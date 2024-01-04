package yjm.value.instruments;

import yjm.value.lang.exceptions.LibraryException;
import yjm.value.pricingengines.PricingEngine;

/**
 * @author Jiaming Yan
 * @description: 金融工具抽象类
 */
public abstract class Instruments {

    private static final String SHOULD_DEFINE_PRICING_ENGINE = "Should define pricing engine";
    private static final String SETUP_ARGUMENTS_NOT_IMPLEMENTED = "Instrument#setupArguments() not implemented";

    protected PricingEngine engine;

    protected double NPV;

    protected double errorEstimate;

    /**
     * 判断交易是否有效
     */
    public abstract boolean isExpired();

    /**
     * 判断交易是否有效
     */
    protected void setupArguments(final PricingEngine.Arguments a)  {
        throw new LibraryException(SETUP_ARGUMENTS_NOT_IMPLEMENTED);
    }
}
