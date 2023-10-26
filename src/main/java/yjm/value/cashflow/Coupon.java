package yjm.value.cashflow;

import yjm.value.daycounters.DayCounter;
import yjm.value.time.Date;
import yjm.value.util.PolymorphicVisitor;
import yjm.value.util.Visitor;


/**
 * Coupon类
 */
public abstract class Coupon extends CashFlow {

    /**
     * 名义本金
     */
    protected double nominal;

    /**
     * 支付日期
     */
    protected Date paymentDate_;

    /**
     * 起息日
     */
    protected Date accrualStartDate_;

    /**
     * 到期日
     */
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

    @Override
    public void accept(final PolymorphicVisitor pv) {
        final Visitor<Coupon> v = (pv!=null) ? pv.visitor(this.getClass()) : null;
        if (v != null) {
            v.visit(this);
        } else {
            super.accept(pv);
        }
    }


}
