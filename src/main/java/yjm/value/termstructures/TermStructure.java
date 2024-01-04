package yjm.value.termstructures;

import yjm.value.daycounters.DayCounter;
import yjm.value.math.interpolations.Extrapolator;
import yjm.value.time.Calendar;
import yjm.value.time.Date;

/**
 * 期限结构接口
 */
public abstract interface TermStructure extends Extrapolator {

    /**
     * 最大日期
     */
    public abstract Date maxDate() ;

    /**
     * 曲线日历
     */
    public abstract Calendar calendar();


    /**
     * settlementDays
     */
    public int settlementDays();


    /**
     * 年化时间
     */
    public abstract double timeFromReference(final Date date);

    /**
     * 日算惯例
     */
    public abstract DayCounter dayCounter();

    /**
     * 最大日期
     */
    public abstract double maxTime();

    /**
     * 返回折现因子为1或方差为0的参考日期
     */
    public abstract Date referenceDate();

}