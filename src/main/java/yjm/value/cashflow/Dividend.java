package yjm.value.cashflow;

import yjm.value.QL;
import yjm.value.time.Date;

import java.util.ArrayList;
import java.util.List;

/**
 * Dividend
 */
public abstract class Dividend extends CashFlow {

	protected Date date;

	public Dividend(final Date date) {
		super();
		this.date = date;
	}

	@Override
	public Date date() {
		return date;
	}

	public abstract double amount(final double underlying);

    /**
     * 生成Dividend列表
     */
	public static List<? extends Dividend> DividendVector(final List<Date> dividendDates, final List<Double> dividends) {
	    QL.require(dividendDates.size() == dividends.size() , "Dividend日期与金额数量不匹配");
        final List<Dividend> items = new ArrayList<Dividend>(dividendDates.size());
        for (int i=0; i<dividendDates.size(); i++) {
            items.add(new FixedDividend(dividends.get(i), dividendDates.get(i)));
        }
        return items;
    }


}
