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
import yjm.value.lang.iterators.Iterables;
import yjm.value.math.Closeness;
import yjm.value.math.Constants;
import yjm.value.time.Calendar;
import yjm.value.time.Date;
import yjm.value.time.TimeSeries;
import yjm.value.util.DefaultObservable;
import yjm.value.util.Observable;
import yjm.value.util.Observer;

import java.util.Iterator;
import java.util.List;

/**
 * indexes
 */

public abstract class Index implements Observable {

	private final Observable delegatedObservable = new DefaultObservable(this);

    /**
	 * 返回index名称
	 */
	public abstract String name();

	/**
	 * 返回定盘日历
	 */
	public abstract Calendar fixingCalendar();

	/**
	 * 判断定盘日是否有效
	 */
	public abstract boolean isValidFixingDate(Date fixingDate);

	/**
	 * 计算定盘利率
	 */
	public abstract double fixing(Date fixingDate, boolean forecastTodaysFixing);

	/**
	 * 定盘数据
	 */
	public TimeSeries<Double> timeSeries() {
		return IndexManager.getInstance().getHistory(name());
	}
	
	/**
	 * 添加/修改定盘利率
	 */
	public void addFixing(final Date date, final double value) {
		addFixing(date, value, false);
	}

	/**
	 * 添加/修改定盘利率
	 */
	public void addFixing(final Date date, final double value, final boolean forceOverwrite) {
		final String tag = name();
		boolean missingFixing;
		boolean validFixing;
		boolean noInvalidFixing = true;
		boolean noDuplicatedFixing = true;
		final TimeSeries<Double> h = IndexManager.getInstance().getHistory(tag);
        validFixing = isValidFixingDate(date); // 是否为有效的定盘日
        final Double currentValue = h.get(date);

		// 如果强制重写或值为无穷大，则为true
        missingFixing = forceOverwrite || Closeness.isClose(currentValue, Constants.NULL_REAL);

        if (validFixing) {
            if (missingFixing) {
                h.put(date, value);
            } else if (Closeness.isClose(currentValue, value)) {
                // 重写的值与之前的数据差距小，什么都不做
            } else {
				// 重写的值与之前的数据差距大
                noDuplicatedFixing = false;
            }
        } else {
			// 不是有效的定盘日期
            noInvalidFixing = false;
        }

		// 更新定盘利率数据
		IndexManager.getInstance().setHistory(tag, h);

		QL.ensure(noInvalidFixing , "存在无效的定盘利率");
		QL.ensure(noDuplicatedFixing , "存在重复的定盘利率");
	}

	/**
	 * 用后一位数据补充/修改
	 */
	public final void addFixings(final Iterator<Date> dates, final Iterator<Double> values, final boolean forceOverwrite) {
		final String tag = name();
		boolean missingFixing;
		boolean validFixing;
		boolean noInvalidFixing = true;
		boolean noDuplicatedFixing = true;
		final TimeSeries<Double> h = IndexManager.getInstance().getHistory(tag);

		for (final Date date : Iterables.unmodifiableIterable(dates)) {
            final double value = values.next();
            validFixing = isValidFixingDate(date);
            final double currentValue = h.get(date);
            missingFixing = forceOverwrite || Closeness.isClose(currentValue, Constants.NULL_REAL);
            if (validFixing) {
                if (missingFixing) {
                    h.put(date, value);
                } else if (Closeness.isClose(currentValue, value)) {
                    // Do nothing
                } else {
                    noDuplicatedFixing = false;
                }
            } else {
                noInvalidFixing = false;
            }
		}

		IndexManager.getInstance().setHistory(tag, h);

		QL.ensure(noInvalidFixing , "存在无效的定盘利率");
		QL.ensure(noDuplicatedFixing , "存在重复的定盘利率");
	}

	public final void clearFixings() {
		IndexManager.getInstance().clearHistory(name());
	}


	// implements Observable

	/**
	 * 计算定盘利率
	 */
	public double fixing(final Date fixingDate){
        return fixing(fixingDate, false);
    }

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
