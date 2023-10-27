package yjm.value.currencies;

import yjm.value.math.Rounding;

public class Asia {

    /**
     * Bangladesh taka
     */
    public static class BDTCurrency extends Currency {
        public BDTCurrency() {
            Data bdtData =
                    new Data("Bangladesh taka", "BDT", 50,
                            "Bt", "", 100,
                            new Rounding(),
                            "%3% %1$.2f");
            data = bdtData;
        }
    }


    /**
     * Chinese yuan
     */
    public static class CNYCurrency extends Currency {
        public CNYCurrency() {
            Data cnyData
                    = new Data("Chinese yuan", "CNY", 156,
                    "Y", "", 100,
                    new Rounding(),
                    "%3% %1$.2f");
            data = cnyData;
        }
    }



    /**
     * Honk Kong dollar
     */
    public static class HKDCurrency extends Currency {
        public HKDCurrency() {
            Data hkdData
                    = new Data("Honk Kong dollar", "HKD", 344,
                    "HK$", "", 100,
                    new Rounding(),
                    "%3% %1$.2f");
            data = hkdData;
        }
    }



    /**
     * Israeli shekel
     */
    public static class ILSCurrency extends Currency {
        public ILSCurrency() {
            Data ilsData
                    = new Data("Israeli shekel", "ILS", 376,
                    "NIS", "", 100,
                    new Rounding(),
                    "%1$.2f %3%");
            data = ilsData;
        }
    }



    /**
     * Indian rupee
     */
    public static class INRCurrency extends Currency {
        public INRCurrency() {
            Data inrData
                    = new Data("Indian rupee", "INR", 356,
                    "Rs", "", 100,
                    new Rounding(),
                    "%3% %1$.2f");
            data = inrData;
        }
    }



    /**
     * Iraqi dinar
     */
    public static class IQDCurrency extends Currency {
        public IQDCurrency() {
            Data iqdData
                    = new Data("Iraqi dinar", "IQD", 368,
                    "ID", "", 1000,
                    new Rounding(),
                    "%2% %1$.3f");
            data = iqdData;
        }
    }



    /**
     * Iranian rial
     */
    public static class IRRCurrency extends Currency {
        public IRRCurrency() {
            Data irrData
                    = new Data("Iranian rial", "IRR", 364,
                    "Rls", "", 1,
                    new Rounding(),
                    "%3% %1$.2f");
            data = irrData;
        }
    }



    /**
     * Japanese yen
     */
    public static class JPYCurrency extends Currency {
        public JPYCurrency() {
            Data jpyData
                    = new Data("Japanese yen", "JPY", 392,
                    "\\xA5", "", 100,
                    new Rounding(),
                    "%3% %1$.0f");
            data = jpyData;
        }
    }



    /**
     * South-Korean won
     */
    public static class KRWCurrency extends Currency {
        public KRWCurrency() {
            Data krwData
                    = new Data("South-Korean won", "KRW", 410,
                    "W", "", 100,
                    new Rounding(),
                    "%3% %1$.0f");
            data = krwData;
        }
    }


    /**
     * Kuwaiti dinar
     */
    public static class KWDCurrency extends Currency {
        public KWDCurrency() {
            Data kwdData
                    = new Data("Kuwaiti dinar", "KWD", 414,
                    "KD", "", 1000,
                    new Rounding(),
                    "%3% %1$.3f");
            data = kwdData;
        }
    }



    /**
     * Nepal rupee
     */
    public static class NPRCurrency extends Currency {
        public NPRCurrency() {
            Data nprData
                    = new Data("Nepal rupee", "NPR", 524,
                    "NRs", "", 100,
                    new Rounding(),
                    "%3% %1$.2f");
            data = nprData;
        }
    }



    /**
     * Pakistani rupee
     */
    public static class PKRCurrency extends Currency {
        public PKRCurrency() {
            Data pkrData
                    = new Data("Pakistani rupee", "PKR", 586,
                    "Rs", "", 100,
                    new Rounding(),
                    "%3% %1$.2f");
            data = pkrData;
        }
    }



    /**
     * Saudi riyal
     */
    public static class SARCurrency extends Currency {
        public SARCurrency() {
            Data sarData
                    = new Data("Saudi riyal", "SAR", 682,
                    "SRls", "", 100,
                    new Rounding(),
                    "%3% %1$.2f");
            data = sarData;
        }
    }



    /**
     * Singapore dollar
     */
    public static class SGDCurrency extends Currency {
        public SGDCurrency() {
            Data sgdData
                    = new Data("Singapore dollar", "SGD", 702,
                    "S$", "", 100,
                    new Rounding(),
                    "%3% %1$.2f");
            data = sgdData;
        }
    }



    /**
     * Thai baht
     */
    public static class THBCurrency extends Currency {
        public THBCurrency() {
            Data thbData
                    = new Data("Thai baht", "THB", 764,
                    "Bht", "", 100,
                    new Rounding(),
                    "%1$.2f %3%");
            data = thbData;
        }
    }


    /**
     * Taiwan dollar
     */
    public static class TWDCurrency extends Currency {
        public TWDCurrency() {
            Data twdData
                    = new Data("Taiwan dollar", "TWD", 901,
                    "NT$", "", 100,
                    new Rounding(),
                    "%3% %1$.2f");
            data = twdData;
        }
    }

}
