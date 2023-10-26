/*
 Copyright (C) 2008 Richard Gomes

 This source code is release under the BSD License.

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the JQuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.

 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

/*
 Copyright (C) 2004, 2005, 2006 StatPro Italia srl

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 */

package yjm.value.termstructures;


import yjm.value.daycounters.DayCounter;
import yjm.value.time.Date;
import yjm.value.time.Frequency;
import yjm.value.time.Period;

/**
 * 利率期限结构
 */
public abstract interface YieldTermStructure extends TermStructure {

    /**
     * 计算即期利率
     */
    public abstract InterestRate zeroRate(
            final Date d,
            final DayCounter resultDayCounter,
            final Compounding comp);

    /**
     * 计算即期利率
     */
    public abstract InterestRate zeroRate(
            final Date d,
            final DayCounter resultDayCounter,
            final Compounding comp,
            final Frequency freq);

    /**
     * 计算即期利率
     */
    public abstract InterestRate zeroRate(
            final Date d,
            final DayCounter dayCounter,
            final Compounding comp,
            final Frequency freq,
            boolean extrapolate);

    /**
     * 计算即期利率，传入年化时间
     */
    public abstract InterestRate zeroRate(
            final double time,
            final Compounding comp,
            final Frequency freq,
            boolean extrapolate);

    /**
     * 计算远期利率
     */
    public abstract InterestRate forwardRate(
            final Date d1,
            final Date d2,
            final DayCounter resultDayCounter,
            final Compounding comp);

    /**
     * 计算远期利率
     */
    public abstract InterestRate forwardRate(
            final Date d1,
            final Date d2,
            final DayCounter resultDayCounter,
            final Compounding comp,
            final Frequency freq);

    /**
     * 计算远期利率
     */
    public abstract InterestRate forwardRate(
            final Date d1,
            final Date d2,
            final DayCounter dayCounter,
            final Compounding comp,
            final Frequency freq,
            boolean extrapolate);

    /**
     * 计算远期利率，传入年化时间
     */
    public abstract InterestRate forwardRate(
            final double t1,
            final double t2,
            final Compounding comp);

    /**
     * 计算远期利率，传入年化时间
     */
    public abstract InterestRate forwardRate(
            final double t1,
            final double t2,
            final Compounding comp,
            final Frequency freq);

    /**
     * 计算远期利率，传入年化时间
     */
    public abstract InterestRate forwardRate(
            final double time1,
            final double time2,
            final Compounding comp,
            final Frequency freq,
            boolean extrapolate);

    /**
     * 计算远期利率
     */
    public abstract InterestRate forwardRate(
            final Date d,
            final Period p,
            final DayCounter resultDayCounter,
            Compounding comp,
            Frequency freq);

    /**
     * 计算远期利率
     */
    public abstract InterestRate forwardRate(
            final Date d,
            final Period p,
            final DayCounter dayCounter,
            final Compounding comp,
            final Frequency freq,
            boolean extrapolate);

    /**
     * 计算折现因子
     */
    public abstract double discount(final Date d);

    /**
     * 计算折现因子
     */
    public abstract double discount(final Date d, boolean extrapolate);

    /**
     * 计算折现因子
     */
    public abstract double discount(final double t);

    /**
     * 计算折现因子
     */
    public abstract double discount(final double t, boolean extrapolate);

    /**
     * 计算票面利率
     */
    public abstract double parRate(int tenor, final Date startDate, final Frequency freq, boolean extrapolate);

    /**
     * 计算票面利率
     */
    public abstract double parRate(final Date[] dates, final Frequency freq, boolean extrapolate);

    /**
     * 计算票面利率
     */
    public abstract double parRate(final double[] times, final Frequency frequency, boolean extrapolate);

}