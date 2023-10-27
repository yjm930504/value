package yjm.value;

import yjm.value.time.Date;
import java.util.Map;
import java.util.TreeMap;

public class Settings {

    /**
     * 允许负利率
     */
    private static final String NEGATIVE_RATES = "NEGATIVE_RATES";

    /**
     * 额外安全检查
     */
    private static final String EXTRA_SAFETY_CHECKS = "EXTRA_SAFETY_CHECKS";

    /**
     * 当日的支付进入NPV计算
     */
    private static final String TODAYS_PAYMENTS = "TODAYS_PAYMENTS";

    /**
     * 浮动leg使用基准利率的coupon
     */
    private static final String USE_INDEXED_COUPON = "USE_INDEXED_COUPON";

    /**
     * 使用当天作为历史定盘利率
     */
    private static final String ENFORCES_TODAYS_HISTORIC_FIXINGS = "ENFORCES_TODAYS_HISTORIC_FIXINGS";
    private static final String REFINE_TO_FULL_MACHINE_PRECISION_USING_HALLEYS_METHOD = "REFINE_TO_FULL_MACHINE_PRECISION_USING_HALLEYS_METHOD";
    private static final String EVALUATION_DATE = "EVALUATION_DATE";

    public boolean isNegativeRates() {
        final Object var = attrs.get().get(NEGATIVE_RATES);
        return var==null? false : (Boolean) var;
    }

    public boolean isExtraSafetyChecks() {
        final Object var = attrs.get().get(EXTRA_SAFETY_CHECKS);
        return var==null? false : (Boolean) var;
    }

    public boolean isTodaysPayments() {
        final Object var = attrs.get().get(TODAYS_PAYMENTS);
        return var==null? false : (Boolean) var;
    }

    public boolean isUseIndexedCoupon() {
        final Object var = attrs.get().get(USE_INDEXED_COUPON);
        return var==null? false : (Boolean) var;
    }

    public boolean isEnforcesTodaysHistoricFixings() {
        final Object var = attrs.get().get(ENFORCES_TODAYS_HISTORIC_FIXINGS);
        return var==null? false : (Boolean) var;
    }

    public boolean isRefineHighPrecisionUsingHalleysMethod() {
        final Object var = attrs.get().get(REFINE_TO_FULL_MACHINE_PRECISION_USING_HALLEYS_METHOD);
        return var==null? false : (Boolean) var;
    }

    public void setNegativeRates(final boolean negativeRates) {
        attrs.get().put(NEGATIVE_RATES, negativeRates);
    }

    public void setExtraSafetyChecks(final boolean extraSafetyChecks) {
        attrs.get().put(EXTRA_SAFETY_CHECKS, extraSafetyChecks);
    }

    public void setTodaysPayments(final boolean todaysPayments) {
        attrs.get().put(TODAYS_PAYMENTS, todaysPayments);
    }

    public void setUseIndexedCoupon(final boolean todaysPayments) {
        attrs.get().put(USE_INDEXED_COUPON, todaysPayments);
    }

    public void setEnforcesTodaysHistoricFixings(final boolean enforceTodaysHistoricFixings) {
        attrs.get().put(ENFORCES_TODAYS_HISTORIC_FIXINGS, enforceTodaysHistoricFixings);
    }

    public void setRefineHighPrecisionUsingHalleysMethod(final boolean refineToFullMachinePrecisionUsingHalleysMethod) {
        attrs.get().put(REFINE_TO_FULL_MACHINE_PRECISION_USING_HALLEYS_METHOD, refineToFullMachinePrecisionUsingHalleysMethod);
    }

    public Date evaluationDate() {
        return ((DateProxy) attrs.get().get(EVALUATION_DATE)).value();
    }

    public Date setEvaluationDate(final Date evaluationDate) {
        final DateProxy proxy = (DateProxy) attrs.get().get(EVALUATION_DATE);
        proxy.assign(evaluationDate);
        return proxy;
    }

    private static final ThreadAttributes attrs = new ThreadAttributes();

    private static class ThreadAttributes extends ThreadLocal<Map<String,Object>> {
        @Override
        public Map<String,Object> initialValue() {
            final Map<String, Object> map = new TreeMap<String, Object>();
            map.put(ENFORCES_TODAYS_HISTORIC_FIXINGS, false);
            map.put(NEGATIVE_RATES, false);
            map.put(EXTRA_SAFETY_CHECKS, true);
            map.put(TODAYS_PAYMENTS, true);
            map.put(USE_INDEXED_COUPON, false);
            map.put(REFINE_TO_FULL_MACHINE_PRECISION_USING_HALLEYS_METHOD, false);
            map.put(EVALUATION_DATE, new DateProxy());
            return map;
        }
    }

    private static class DateProxy extends Date {
        
        private DateProxy() {
            super();
        }

        /**
         * 默认返回当天日期
         */
        private DateProxy value() {
            if (isNull()) {
                super.assign(todaysSerialNumber());
            }
            return this;
        }

        private Date assign(final Date date) {
            super.assign(date.serialNumber());
            super.notifyObservers();
            return this;
        }

    }

}
