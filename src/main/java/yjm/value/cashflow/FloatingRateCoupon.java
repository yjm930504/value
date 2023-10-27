package yjm.value.cashflow;

import yjm.value.QL;
import yjm.value.Settings;
import yjm.value.daycounters.DayCounter;
import yjm.value.indexes.InterestRateIndex;
import yjm.value.quotes.Handle;
import yjm.value.termstructures.YieldTermStructure;
import yjm.value.time.BusinessDayConvention;
import yjm.value.time.Date;
import yjm.value.time.Period;
import yjm.value.time.TimeUnit;
import yjm.value.util.Observer;
import yjm.value.util.PolymorphicVisitor;
import yjm.value.util.Visitor;


public class FloatingRateCoupon extends Coupon implements Observer {

    protected InterestRateIndex index_;
    protected DayCounter dayCounter_;
    protected int fixingDays_;
    protected double gearing_;
    protected double spread_;
    protected boolean isInArrears_;
    protected FloatingRateCouponPricer pricer_;

    public FloatingRateCoupon(
            final Date paymentDate,
            final double nominal,
            final Date startDate,
            final Date endDate,
            final int fixingDays,
            final InterestRateIndex index,
            final double gearing,
            final double spread,
            final Date refPeriodStart,
            final Date refPeriodEnd,
            final DayCounter dayCounter,
            final boolean isInArrears) {
        super(nominal, paymentDate, startDate, endDate, refPeriodStart, refPeriodEnd);

        this.index_ = index;
        this.dayCounter_ = dayCounter;
        this.fixingDays_ = fixingDays == 0 ? index.fixingDays() : fixingDays;
        this.gearing_ = gearing;
        this.spread_ = spread;
        this.isInArrears_ = isInArrears;

        QL.require(gearing != 0 , "Null gearing not allowed");

        if (dayCounter_.empty())
            dayCounter_ = index.dayCounter();

        Date evaluationDate = new Settings().evaluationDate();
        this.index_.addObserver(this);
        evaluationDate.addObserver(this);
    }

    public void setPricer(final FloatingRateCouponPricer pricer){
        if (pricer_ != null) {
        	pricer_.deleteObserver(this);
        }
        pricer_ = pricer;
        if ( pricer_ != null ) {
        	pricer_.addObserver(this);
        }
        update();
    }

    public FloatingRateCouponPricer pricer() {
        return pricer_;
    }

    public double amount() {
        return rate() * accrualPeriod() * nominal();
    }

    public  double accruedAmount(final Date d) {

        if (d.le(accrualStartDate_) || d.gt(paymentDate_)) {
            return 0.0;
        } else {
            return nominal() * rate() *
                dayCounter().yearFraction(accrualStartDate_,
                                          Date.min(d,accrualEndDate_),
                                          refPeriodStart_,
                                          refPeriodEnd_);
        }
    }

    public double price(final Handle<YieldTermStructure> yts) {
        return amount() * yts.currentLink().discount(date());
    }

    public DayCounter dayCounter() {
        return dayCounter_;
    }

    public InterestRateIndex index() {
        return index_;
    }

    public int fixingDays() {
        return fixingDays_;
    }

    public Date fixingDate() {
        // if isInArrears_ fix at the end of period
        Date refDate = isInArrears_ ? accrualEndDate_ : accrualStartDate_;
        return index_.fixingCalendar().advance(refDate, new Period(-fixingDays_, TimeUnit.Days), BusinessDayConvention.Preceding);
    }


    public double gearing() {
        return gearing_;
    }
    public double spread() {
        return spread_;
    }

    public /*Rate*/ double indexFixing() {
        return index_.fixing(fixingDate());
    }

    public /*Rate*/ double rate() {
        QL.require(pricer_ != null, "pricer not set");
        pricer_.initialize(this);
        return pricer_.swapletRate();
    }

    public /*Rate*/ double adjustedFixing()  {
        return (rate()-spread())/gearing();
    }

    public boolean isInArrears() {
        return isInArrears_;
    }

    public /*Rate*/ double convexityAdjustmentImpl(double /*Rate*/ f) {
        return (gearing() == 0.0 ? 0.0 : adjustedFixing()-f);
    }

    public /*Rate*/ double convexityAdjustment() {
        return convexityAdjustmentImpl(indexFixing());
    }

    public void update() {
        notifyObservers();
    }

    //
    // implements TypeVisitable
    //

    @Override
    public void accept(final PolymorphicVisitor pv) {
        final Visitor<FloatingRateCoupon> v = (pv!=null) ? pv.visitor(this.getClass()) : null;
        if (v != null) {
            v.visit(this);
        } else {
            super.accept(pv);
        }
    }
}
