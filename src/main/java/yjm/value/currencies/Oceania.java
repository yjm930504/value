package yjm.value.currencies;

import yjm.value.math.Rounding;

public class Oceania {

    /**
     * Australian dollar
     */
    public static class AUDCurrency extends Currency {
        public AUDCurrency() {
            Data audData = new Data("Australian dollar", "AUD", 36,
                    "A$", "", 100,
                    new Rounding(),
                    "%3% %1$.2f");
            data = audData;
        }
    }

    /**
     * New Zealand dollar
     */
    public static class NZDCurrency extends Currency {
        public NZDCurrency() {
            Data nzdData = new Data("New Zealand dollar", "NZD", 554,
                    "NZ$", "", 100,
                    new Rounding(),
                    "%3% %1$.2f");
            data = nzdData;
        }
    }

}
