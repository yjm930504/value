package yjm.value.termstructures;

import yjm.value.QL;
import yjm.value.Settings;
import yjm.value.daycounters.Actual365Fixed;
import yjm.value.daycounters.DayCounter;
import yjm.value.math.Closeness;
import yjm.value.math.interpolations.DefaultExtrapolator;
import yjm.value.math.interpolations.Extrapolator;
import yjm.value.time.Calendar;
import yjm.value.time.Date;
import yjm.value.time.TimeUnit;
import yjm.value.util.DefaultObservable;
import yjm.value.util.Observable;
import yjm.value.util.Observer;
import java.util.List;


/**
 * 期限结机构抽象类
 */
public abstract class AbstractTermStructure implements TermStructure {

    private static final String THIS_METHOD_MUST_BE_OVERRIDDEN = "必须重写此方法";

    private Date referenceDate;

    private final DayCounter dayCounter;

    private boolean updated;

    private final int settlementDays;

    private final boolean moving;

    protected Calendar calendar;

    public AbstractTermStructure() {
        this(new Actual365Fixed());
    }

    /**
     * <p>This constructor requires an override of method {@link TermStructure#referenceDate()} in
     * derived classes so that it fetches and return the appropriate reference date.
     * This is the <i>Case 3</i> described on the top of this class.
     *
     * @see TermStructure documentation for more details about constructors.
     */
    //TODO : What's the calendar in this case?
    public AbstractTermStructure(final DayCounter dc) {
        QL.require(dc!=null , "day counter must be informed"); // TODO: message
        this.calendar = null;
        this.settlementDays = 0;
        this.dayCounter = dc;

        // When Case 1 or Case 3
        this.moving = false;
        this.updated = true;

        // initialize reference date without any observers
        this.referenceDate = null;
    }

    /**
     * Initialize with a fixed reference date
     *
     * <p>This constructor takes a date to be used.
     * The default implementation of {@link TermStructure#referenceDate()} will
     * then return such date.
     * This is the <i>Case 1</i> described on the top of this class.
     *
     * @see TermStructure documentation for more details about constructors.
     */
    public AbstractTermStructure(final Date referenceDate, final Calendar calendar) {
        this(referenceDate, calendar, new Actual365Fixed());
    }

    /**
     * Initialize with a fixed reference date
     *
     * <p>This constructor takes a date to be used.
     * The default implementation of {@link TermStructure#referenceDate()} will
     * then return such date.
     * This is the <i>Case 1</i> described on the top of this class.
     *
     * @see TermStructure documentation for more details about constructors.
     */
    public AbstractTermStructure(final Date referenceDate, final Calendar calendar, final DayCounter dc) {
        QL.require(referenceDate!=null , "reference date must be informed"); // TODO: message
        QL.require(calendar!=null , "calendar must be informed"); // TODO: message
        QL.require(dc!=null , "day counter must be informed"); // TODO: message

        this.settlementDays = 0;
        this.calendar = calendar;
        this.dayCounter = dc;

        // When Case 1 or Case 3
        this.moving = false;
        this.updated = true;

        // initialize reference date with this class as observer
        this.referenceDate = referenceDate;
    }

    /**
     * Calculate the reference date based on the global evaluation date
     *
     * <p>This constructor takes a number of days and a calendar to be used
     * so that {@link TermStructure#referenceDate()} will return a date calculated based on the
     * current evaluation date and the term structure. This class will be notified when the
     * evaluation date changes.
     * This is the <i>Case 2</i> described on the top of this class.
     *
     * @see TermStructure documentation for more details about constructors.
     */
    public AbstractTermStructure(final int settlementDays, final Calendar calendar) {
        this(settlementDays, calendar, new Actual365Fixed());
    }


    /**
     * Calculate the reference date based on the global evaluation date
     *
     * <p>This constructor takes a number of days and a calendar to be used
     * so that {@link TermStructure#referenceDate()} will return a date calculated based on the
     * current evaluation date and the term structure. This class will be notified when the
     * evaluation date changes.
     * This is the <i>Case 2</i> described on the top of this class.
     *
     * @see TermStructure documentation for more details about constructors.
     */
    public AbstractTermStructure(final int settlementDays, final Calendar calendar, final DayCounter dc) {
        this.settlementDays = settlementDays;
        this.calendar = calendar;
        this.dayCounter = dc;

        // When Case 2
        this.moving = true;
        this.updated = false;

        // observes date changes
        final Date today = new Settings().evaluationDate();
        today.addObserver(this);

        this.referenceDate = calendar.advance(today, settlementDays, TimeUnit.Days);
    }


    //
    // protected methods
    //

    /**
     * This method performs date-range check
     */
    protected void checkRange(final Date d, final boolean extrapolate) /* @ReadOnly */ {
        QL.require(d.ge(referenceDate()) , "date before reference date"); // TODO: message
        QL.require(extrapolate || allowsExtrapolation() || d.le(maxDate()) , "date is past max curve"); // TODO: message
    }

    /**
     * This method performs date-range check
     */
    protected void checkRange(/*@Time*/ final double t, final boolean extrapolate) /* @ReadOnly */ {
        QL.require(t >= 0.0 , "negative time given"); // TODO: message
        QL.require(extrapolate||allowsExtrapolation()||t<=maxTime()||Closeness.isCloseEnough(t, maxTime()) , "time is past max curve"); // TODO: message
    }


    //
    // implements TermStructure
    //

    /* (non-Javadoc)
     * @see yjm.value.termstructures.TermStructure#calendar()
     */
    @Override
    public Calendar calendar() /* @ReadOnly */ {
        QL.require(this.calendar != null , THIS_METHOD_MUST_BE_OVERRIDDEN); // TODO: message
        return calendar;
    }

    /* (non-Javadoc)
     * @see yjm.value.termstructures.TermStructure#settlementDays()
     */
    public /*@Natural*/ int settlementDays() /* @ReadOnly */ {
        return settlementDays;
    }

    /* (non-Javadoc)
     * @see yjm.value.termstructures.TermStructure#timeFromReference(yjm.value.util.Date)
     */
    @Override
    public final /*@Time*/ double timeFromReference(final Date date) /* @ReadOnly */ {
        return dayCounter().yearFraction(referenceDate(), date);
    }

    /* (non-Javadoc)
     * @see yjm.value.termstructures.TermStructure#dayCounter()
     */
    @Override
    public DayCounter dayCounter() /* @ReadOnly */ {
        QL.require(this.dayCounter != null , THIS_METHOD_MUST_BE_OVERRIDDEN); // TODO: message
        return dayCounter;
    }

    /* (non-Javadoc)
     * @see yjm.value.termstructures.TermStructure#maxTime()
     */
    @Override
    public /*@Time*/ double maxTime() /* @ReadOnly */ {
        return timeFromReference(maxDate());
    }

    /* (non-Javadoc)
     * @see yjm.value.termstructures.TermStructure#referenceDate()
     */
    @Override
    public Date referenceDate() /* @ReadOnly */ {
        if (!updated) {
        	final Date today = new Settings().evaluationDate();
        	referenceDate = calendar().advance(today, settlementDays, TimeUnit.Days);
            updated = true;
        }
        return referenceDate;
    }


    //
    // implements Extrapolator
    //

    /**
     * Implements multiple inheritance via delegate pattern to a inner class
     *
     * @see Extrapolator
     */
    private final DefaultExtrapolator delegatedExtrapolator = new DefaultExtrapolator();

    /**
     * @return
     */
    @Override
    public final boolean allowsExtrapolation() {
        return delegatedExtrapolator.allowsExtrapolation();
    }

    @Override
    public void disableExtrapolation() {
        delegatedExtrapolator.disableExtrapolation();
    }

    @Override
    public void enableExtrapolation() {
        delegatedExtrapolator.enableExtrapolation();
    }


    //
    // implements Observer
    //

    //XXX:registerWith
    //    @Override
    //    public void registerWith(final Observable o) {
    //        o.addObserver(this);
    //    }
    //
    //    @Override
    //    public void unregisterWith(final Observable o) {
    //        o.deleteObserver(this);
    //    }

    @Override
    //XXX::OBS public void update(final Observable o, final Object arg) {
    public void update() {
        if (moving) {
            updated = false;
        }
        notifyObservers();
    }


    //
    // implements Observable
    //

    /**
     * Implements multiple inheritance via delegate pattern to an inner class
     *
     * @see Observable
     * @see DefaultObservable
     */
    private final Observable delegatedObservable = new DefaultObservable(this);

    @Override
    public void addObserver(final Observer observer) {
        delegatedObservable.addObserver(observer);
    }

    @Override
    public int countObservers() {
        return delegatedObservable.countObservers();
    }

    @Override
    public void deleteObserver(final Observer observer) {
        delegatedObservable.deleteObserver(observer);
    }

    @Override
    public void notifyObservers() {
        delegatedObservable.notifyObservers();
    }

    @Override
    public void notifyObservers(final Object arg) {
        delegatedObservable.notifyObservers(arg);
    }

    @Override
    public void deleteObservers() {
        delegatedObservable.deleteObservers();
    }

    @Override
    public List<Observer> getObservers() {
        return delegatedObservable.getObservers();
    }

}
