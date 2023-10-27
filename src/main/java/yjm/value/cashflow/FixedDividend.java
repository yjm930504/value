package yjm.value.cashflow;

import yjm.value.time.Date;


/**
 * 固定股息类
 */
public class FixedDividend extends Dividend {

	protected double amount;

	public FixedDividend(final double amount, final Date date) {
		super(date);
		this.amount = amount;
	}

	@Override
	public double amount(final double underlying) {
		return amount;
	}

	@Override
	public double amount() {
		return amount;
	}

}
