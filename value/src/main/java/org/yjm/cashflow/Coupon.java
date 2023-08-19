package org.yjm.cashflow;

import org.yjm.daycounters.DayCounter;
import org.yjm.time.Date;
public abstract class Coupon extends Cashflow {

    protected double nominal;
    protected Date paymentDate_;
    protected Date accrualStartDate_;
    protected Date accrualEndDate_;
    protected Date refPeriodStart_;
    protected Date refPeriodEnd_;

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    public Coupon(final double nominal,
                  final Date paymentDate,
                  final Date accrualStartDate,
                  final Date accrualEndDate){
        this(nominal, paymentDate, accrualStartDate, accrualEndDate, new Date(), new Date());
    }

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    public Coupon(final double nominal,
                  final Date paymentDate,
                  final Date accrualStartDate,
                  final Date accrualEndDate,
                  final Date refPeriodStart,
                  final Date refPeriodEnd){

        this.nominal = nominal;
        this.paymentDate_ = paymentDate.clone();
        this.accrualStartDate_ = accrualStartDate.clone();
        this.accrualEndDate_ = accrualEndDate.clone();
        this.refPeriodStart_ = refPeriodStart.clone();
        this.refPeriodEnd_ = refPeriodEnd.clone();

    }

    public abstract double rate();

    public abstract DayCounter dayCounter();

    public abstract double accruedAmount(final Date date);

    public double nominal(){
        return nominal;
    }

    public Date accrualStartDate(){
        return accrualStartDate_;
    }

    public Date accrualEndDate(){
        return accrualEndDate_;
    }

    public Date referencePeriodStart() {
        return refPeriodStart_;
    }

    public Date referencePeriodEnd() {
        return refPeriodEnd_;
    }


    /**
     * @Author  Jiaming Yan
     * @Description 根据日算惯例，返回计息区间的年化时间
     */
    public double accrualPeriod() {
        return dayCounter().yearFraction(accrualStartDate_,
                accrualEndDate_,
                refPeriodStart_,
                refPeriodEnd_);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 根据日算惯例，返回计息区间的天数
     */
    public long accrualDays() {
        return dayCounter().daycount(accrualStartDate_,
                accrualEndDate_);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回支付时间对象
     */
    public Date date() {
        return paymentDate_.clone();
    }


}
