package yjm.value.currencies;

import yjm.value.math.Rounding;

/**
 * South-African rand
 */
public class Africa {

    public static class ZARCurrency extends Currency {
        public ZARCurrency() {
            Data zarData = new Data(
                    "South-African rand",
                    "ZAR",
                    710,
                    "R",
                    "",
                    100,
                    new Rounding(),
                    "%3% %1$.2f");
            data = zarData;
        }
    }
}
