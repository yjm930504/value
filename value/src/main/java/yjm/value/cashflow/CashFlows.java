package yjm.value.cashflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yjm.value.QL;
import yjm.value.time.Date;

/**
 * Cashflow的分析函数类
 */
public class CashFlows {
    private static final Logger logger = LoggerFactory.getLogger(CashFlows.class);

    private final String not_enough_information_available = "not enough information available";
    private final String no_cashflows = "no cashflows";
    private final String unsupported_compounding_type = "unsupported compounding type";
    private final String compounded_rate_required = "compounded rate required";
    private final String unsupported_frequency = "unsupported frequency";
    private final String unknown_duration_type = "unsupported duration type";
    private final String infeasible_cashflow = "the given cash flows cannot result in the given market price due to their sign";
    private static double basisPoint_ = 1.0e-4;



    private CashFlows() {
    }

    private static volatile CashFlows instance = null;


    /**
     * 返回CashFlows实例
     * @return 现金流实例
     */
    public static CashFlows getInstance() {
        if (instance == null) {
            synchronized (CashFlows.class) {
                if (instance == null) {
                    instance = new CashFlows();
                }
            }
        }
        return instance;
    }

    /**
     *
     * @param cashflows
     * @return 现金流起息日
     */
    public Date startDate(final Leg cashflows) {
        Date d = Date.maxDate();
        for (int i = 0; i < cashflows.size(); ++i) {
            final Coupon c = (Coupon) cashflows.get(i);
            if (c != null) {
                d = Date.min(c.accrualStartDate(), d);
            }
        }
        QL.ensure(d.lt(Date.maxDate()) , not_enough_information_available);
        return d;
    }

    /**
     *
     * @param cashflows
     * @return 现金流到期
     */
    public Date maturityDate(final Leg cashflows) {
        Date d = Date.minDate();
        for (int i = 0; i < cashflows.size(); i++) {
            d = Date.max(d, cashflows.get(i).date());
        }
        QL.ensure (d.gt(Date.minDate()), no_cashflows);
        return d;
    }

    /**
     *
     * @param cashflows
     * @param discountCurve
     * @param settlementDate
     * @param npvDate
     * @return npv
     */
    public double npv(
            final Leg cashflows,
            final Handle<YieldTermStructure> discountCurve,
            final Date settlementDate,
            final Date npvDate) {
        return npv(cashflows, discountCurve, settlementDate, npvDate, 0);
    }
}
