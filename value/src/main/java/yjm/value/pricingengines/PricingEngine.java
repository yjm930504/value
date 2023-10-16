package yjm.value.pricingengines;

import yjm.value.util.Observable;

public interface PricingEngine extends Observable {

    public final static String PRICING_ENGINE_NOT_SET = "Pricing engine not set";

    public PricingEngine.Arguments getArguments();

    public PricingEngine.Results getResults();

    public abstract void reset();

    public abstract void calculate();

    public abstract void update();

    public abstract interface Arguments {
        public void validate();
    }
    public abstract interface Results {
        public abstract void reset();
    }

}
