package yjm.value.cashflow;

import yjm.value.QL;
import yjm.value.Settings;
import yjm.value.daycounters.DayCounter;
import yjm.value.indexes.InterestRateIndex;
import yjm.value.termstructures.YieldTermStructure;
import yjm.value.time.BusinessDayConvention;
import yjm.value.time.Date;
import yjm.value.time.Period;
import yjm.value.time.TimeUnit;


public class FloatingRateCoupon extends Coupon {

    protected InterestRateIndex index_;
    protected DayCounter dayCounter_;
    protected int fixingDays_;
    protected double gearing_;
    protected double spread_;

    /**
     * 是否期末定盘
     */
    protected boolean isInArrears_;

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

        QL.require(gearing != 0, "gearing为空");

        // 没有日算惯例，使用index的日算惯例
        if (dayCounter_.empty())
            dayCounter_ = index.dayCounter();

        // 估值日
        Date evaluationDate = new Settings().evaluationDate();

    }



    /**
     * 计算浮动利率Coupon金额
     * 本金 * 利率 * 计息区间的年化时间
     */
    public double amount() {
        return rate() * accrualPeriod() * nominal();
    }

    /**
     * 计算应计金额
     */
    public double accruedAmount(final Date d) {

        if (d.le(accrualStartDate_) || d.gt(paymentDate_)) {
            return 0.0;
        } else {
            return nominal() * rate() *
                    dayCounter().yearFraction(accrualStartDate_, Date.min(d, accrualEndDate_), refPeriodStart_, refPeriodEnd_);
        }
    }

    /**
     * 计算coupon现值
     */


    @Override
    public double rate() {
        return 0;
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

    /**
     * 计算定盘日
     */
    public Date fixingDate() {
        Date refDate = isInArrears_ ? accrualEndDate_ : accrualStartDate_;
        return index_.fixingCalendar().advance(refDate, new Period(-fixingDays_, TimeUnit.Days), BusinessDayConvention.Preceding);
    }


    public double gearing() {
        return gearing_;
    }

    public double spread() {
        return spread_;
    }

    /**
     * 计算定盘利率
     */
    public double indexFixing() {
        return index_.fixing(fixingDate());
    }

    /**
     * 调整定盘利率
     */
    public double adjustedFixing() {
        return (rate() - spread()) / gearing();
    }

    public boolean isInArrears() {
        return isInArrears_;
    }

    /**
     * 凸性调整
     */
    public double convexityAdjustmentImpl(double f) {
        return (gearing() == 0.0 ? 0.0 : adjustedFixing() - f);
    }

    /**
     * 凸性调整
     */
    public double convexityAdjustment() {
        return convexityAdjustmentImpl(indexFixing());
    }

}
