/*
 Copyright (C) 2008 Srinivas Hasti

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

package yjm.value.indexes;

import yjm.value.QL;
import yjm.value.Settings;
import yjm.value.currencies.Currency;
import yjm.value.daycounters.DayCounter;
import yjm.value.math.Constants;
import yjm.value.time.Calendar;
import yjm.value.time.Date;
import yjm.value.time.Period;
import yjm.value.time.TimeUnit;


/**
 * 利率指标类，提供计算定盘利率
 */
public abstract class InterestRateIndex extends Index  {

    protected String familyName;
    protected Period tenor;
    protected int fixingDays;
    protected Calendar fixingCalendar;
    protected Currency currency;
    protected DayCounter dayCounter;
    
    public InterestRateIndex(final String familyName,
                             final Period tenor,
                             final int fixingDays,
                             final Currency currency,
                             final Calendar fixingCalendar,
                             final DayCounter dayCounter) {
        this.familyName = familyName;
        this.tenor = tenor;
        this.fixingDays = fixingDays;
        this.fixingCalendar = fixingCalendar;
        this.currency = currency;
        this.dayCounter = dayCounter;
        this.tenor.normalize();

    }

    /**
     * 拼指标名称
     */
    @Override
    public String name() {
        final StringBuilder builder = new StringBuilder(familyName);
        if (tenor.equals(new Period(1,TimeUnit.Days))) {
            if (fixingDays == 0) {
                builder.append("ON");
            } else if (fixingDays == 1) {
                builder.append("TN");
            } else if (fixingDays == 2) {
                builder.append("SN");
            } else {
                builder.append(tenor.getShortFormat());
            }
        } else {
            builder.append(tenor.getShortFormat());
        }
        builder.append(' ').append(dayCounter.name());
        return builder.toString();
    }


    @Override
    public Calendar fixingCalendar() {
        return fixingCalendar;
    }

    /**
     * 根据定盘日历，判断定盘日是否有效
     */
    @Override
    public boolean isValidFixingDate(final Date fixingDate) {
        return fixingCalendar.isBusinessDay(fixingDate);
    }

    public String familyName() {
        return familyName;
    }

    public Period tenor() {
        return tenor;
    }

    /**
     * 回看天数
     */
    public int fixingDays() {
        return fixingDays;
    }

    public Currency currency() {
        return currency;
    }

    public DayCounter dayCounter() {
        return dayCounter;
    }


    /**
     * 计算未来定盘利率
     */
    protected abstract double forecastFixing(Date fixingDate);


    /**
     * 返回到期日
     */
    public abstract Date maturityDate(Date valueDate);

    /**
     * 计算定盘利率
     * @param forecastTodaysFixing:定盘日在未来时是否用估值日数据作为定盘利率
     */
    @Override
    public double fixing(final Date fixingDate, 
    					 final boolean forecastTodaysFixing) {
        QL.require(isValidFixingDate(fixingDate) , "定盘日：" + fixingDate.toString() + "不是工作日");
        final Date today = new Settings().evaluationDate();
        final boolean enforceTodaysHistoricFixings = new Settings().isEnforcesTodaysHistoricFixings();

        if (fixingDate.lt(today) || (fixingDate.equals(today) && enforceTodaysHistoricFixings && !forecastTodaysFixing)) {
            // 取历史index数据
             double pastFixing =
                    IndexManager.getInstance().getHistory(name()).get(fixingDate);
             QL.require(pastFixing != Constants.NULL_REAL,
                          "缺少 "+ fixingDate + "的" + name() + "数据" );
            return pastFixing;
        }

        if ((fixingDate.equals(today)) && !forecastTodaysFixing) {
            try {
                double pastFixing =
                	IndexManager.getInstance().getHistory(name()).get(fixingDate);
                if (pastFixing != Constants.NULL_REAL)
                    return pastFixing;
                else
                    ;
            } catch (final Exception e) {
            }
        }
        // 否则计算未来定盘利率
        return forecastFixing(fixingDate);
    }

    /**
     * 计算定盘利率
     */
    @Override
    public double fixing(final Date fixingDate) {
        return fixing(fixingDate, false);
    }

    /**
     * 计算定盘日
     */
    public Date fixingDate(final Date valueDate) {
        final Date fixingDate = fixingCalendar().advance(valueDate, fixingDays, TimeUnit.Days);
        QL.ensure(isValidFixingDate(fixingDate) , "定盘日" + fixingDate + "不是工作日");
        return fixingDate;
    }

    /**
     * 计算
     */
    public Date valueDate(final Date fixingDate) {
        QL.require(isValidFixingDate(fixingDate) , "定盘日不是工作日");
        return fixingCalendar().advance(fixingDate, fixingDays, TimeUnit.Days);
    } //TODO: 缺少工作日调整？


}
