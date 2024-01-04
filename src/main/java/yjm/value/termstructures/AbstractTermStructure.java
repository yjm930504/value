package yjm.value.termstructures;

import yjm.value.QL;
import yjm.value.Settings;
import yjm.value.daycounters.Actual365Fixed;
import yjm.value.daycounters.DayCounter;
import yjm.value.math.Closeness;
import yjm.value.math.interpolations.DefaultExtrapolator;
import yjm.value.time.Calendar;
import yjm.value.time.Date;
import yjm.value.time.TimeUnit;

import java.util.List;


/**
 * 期限结构抽象类
 * <p>Case 1: The constructor taking a date is to be used.
 * The default implementation of {@link TermStructure#referenceDate()} will
 * then return such date.
 *
 * <p>Case 2: The constructor taking a number of days and a calendar is to be used
 * so that {@link TermStructure#referenceDate()} will return a date calculated based on the
 * current evaluation date and the term structure and observers will be notified when the
 * evaluation date changes.
 *
 * <p>Case 3: The {@link TermStructure#referenceDate()} method must
 * be overridden in derived classes so that it fetches and
 * return the appropriate date.
 *
 */
public abstract class AbstractTermStructure implements TermStructure {

    private static final String THIS_METHOD_MUST_BE_OVERRIDDEN = "必须重写此方法";

    private Date referenceDate;

    private final DayCounter dayCounter;

    private boolean updated;

    private final int settlementDays;

    private final boolean moving;

    protected Calendar calendar;

    /**
     * 构造方法，默认Actual365Fixed
     */
    public AbstractTermStructure() {
        this(new Actual365Fixed());
    }

    /**
     * 构造方法
     */
    public AbstractTermStructure(final DayCounter dc) {
        QL.require(dc!=null , "日算惯例不能为空");
        this.calendar = null;
        this.settlementDays = 0;
        this.dayCounter = dc;

        // When Case 1 or Case 3
        this.moving = false;
        this.updated = true;

        this.referenceDate = null;
    }

    /**
     * 构造方法
     */
    public AbstractTermStructure(final Date referenceDate, final Calendar calendar) {
        this(referenceDate, calendar, new Actual365Fixed());
    }

    /**
     * 构造方法
     */
    public AbstractTermStructure(final Date referenceDate, final Calendar calendar, final DayCounter dc) {
        QL.require(referenceDate!=null , "基准日期不能为空");
        QL.require(calendar!=null , "日历不能为空");
        QL.require(dc!=null , "日算惯例不能为空");

        this.settlementDays = 0;
        this.calendar = calendar;
        this.dayCounter = dc;

        // When Case 1 or Case 3
        this.moving = false;
        this.updated = true;

        this.referenceDate = referenceDate;
    }

    /**
     * 构造方法
     */
    public AbstractTermStructure(final int settlementDays, final Calendar calendar) {
        this(settlementDays, calendar, new Actual365Fixed());
    }

    /**
     * 构造方法
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

        this.referenceDate = calendar.advance(today, settlementDays, TimeUnit.Days);
    }

    /**
     * 校验日期范围
     */
    protected void checkRange(final Date d, final boolean extrapolate){
        QL.require(d.ge(referenceDate()) , "日期应在基准日期之前");
        QL.require(extrapolate || allowsExtrapolation() || d.le(maxDate()) , "日期超过最大日期");
    }

    /**
     * 校验日期范围
     */
    protected void checkRange(final double t, final boolean extrapolate){
        QL.require(t >= 0.0 , "年化时间为负");
        QL.require(extrapolate||allowsExtrapolation()||t<=maxTime()||Closeness.isCloseEnough(t, maxTime()) ,
                "time is past max curve");
    }

    @Override
    public Calendar calendar(){
        QL.require(this.calendar != null , THIS_METHOD_MUST_BE_OVERRIDDEN);
        return calendar;
    }
    
    public int settlementDays(){
        return settlementDays;
    }

    @Override
    public final double timeFromReference(final Date date){
        return dayCounter().yearFraction(referenceDate(), date);
    }

    @Override
    public DayCounter dayCounter(){
        QL.require(this.dayCounter != null , THIS_METHOD_MUST_BE_OVERRIDDEN);
        return dayCounter;
    }

    @Override
    public double maxTime(){
        return timeFromReference(maxDate());
    }

    @Override
    public Date referenceDate(){
        if (!updated) {
        	final Date today = new Settings().evaluationDate();
        	referenceDate = calendar().advance(today, settlementDays, TimeUnit.Days);
            updated = true;
        }
        return referenceDate;
    }

    private final DefaultExtrapolator delegatedExtrapolator = new DefaultExtrapolator();

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

}
