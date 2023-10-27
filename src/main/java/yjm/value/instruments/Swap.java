package yjm.value.instruments;

import yjm.value.QL;
import yjm.value.cashflow.CashFlow;
import yjm.value.cashflow.CashFlows;
import yjm.value.cashflow.Leg;
import yjm.value.time.Date;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author Jiaming Yan
 * @Description Swap类
 */
public class Swap extends Instruments {

    protected List<Leg> legs;
    protected double[] payer;
    protected double[] legNPV;
    protected double[] legBPS;

    /**
     * @Author Jiaming Yan
     * @Description 构造方法，传入leg
     */
    public Swap(final Leg firstLeg, final Leg secondLeg) {

        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }

        this.legs = new ArrayList<Leg>();
        this.payer = new double[2];
        this.legNPV = new double[2];
        this.legBPS = new double[2];
        legs.add(firstLeg);
        legs.add(secondLeg);
        payer[0] = -1.0; // 支付
        payer[1] = +1.0; // 收取

        for (int i = 0; i < legs.size(); i++) {
            for (final CashFlow item : legs.get(i)) {
                item.addObserver(this);
            }
        }
    }

    /**
     * @Author Jiaming Yan
     * @Description 构造方法，传入legs及支付端标记
     */
    public Swap(final List<Leg> legs, final boolean[] payer) {

        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }

        this.legs = legs;
        this.payer = new double[legs.size()];
        Arrays.fill(this.payer, 1.0);
        this.legNPV = new double[legs.size()];
        this.legBPS = new double[legs.size()];

        for (int j = 0; j < this.legs.size(); j++) {
            if (payer[j]) {
                this.payer[j] = -1.0;
            }

            for (int i = 0; i < legs.size(); i++) {
                for (final CashFlow item : legs.get(i)) {
                    item.addObserver(this);
                }
            }
        }
    }

    /**
     * @Author Jiaming Yan
     * @Description 构造方法，空swap
     */
    protected Swap(final int legs) {

        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }

        this.legs = new ArrayList<Leg>();
        this.payer = new double[legs];
        this.legNPV = new double[legs];
        this.legBPS = new double[legs];
    }


    /**
     * 起息日
     */
    public Date startDate() {
        QL.require(legs.size() > 0, "缺少现金流");
        Date d = CashFlows.getInstance().startDate(this.legs.get(0));
        for (int j = 1; j < this.legs.size(); j++) {
            d = Date.min(d, CashFlows.getInstance().startDate(this.legs.get(j)));
        }
        return d;
    }


    // implements Instrument

    @Override
    public boolean isExpired() {
        return false;
    }

    // implements LazyObject
    @Override
    protected void performCalculations() throws ArithmeticException {

    }
}
