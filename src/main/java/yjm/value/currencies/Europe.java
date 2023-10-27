/*
 Copyright (C) 2009 Ueli Hofstetter

 This source code is release under the BSD License.
 
 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the JQuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */
/*
 Copyright (C) 2004, 2005 StatPro Italia srl

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
*/
package yjm.value.currencies;

import yjm.value.math.Rounding;

public class Europe {

    /**
     * Bulgarian lev
     */
    public static class BGLCurrency extends Currency {
        public BGLCurrency() {
            Data bglData = new Data("Bulgarian lev", "BGL", 100, "lv", "", 100, new Rounding(), "%1$.2f %3%");
            data = bglData;
        }
    }

    ;

    /**
     * Belarussian ruble
     */
    public static class BYRCurrency extends Currency {
        public BYRCurrency() {
            Data byrData = new Data("Belarussian ruble", "BYR", 974, "BR", "", 1, new Rounding(), "%2% %1$.0f");
            data = byrData;
        }
    }

    ;

    /**
     * Swiss franc
     */
    public static class CHFCurrency extends Currency {
        public CHFCurrency() {
            Data chfData = new Data("Swiss franc", "CHF", 756, "SwF", "", 100, new Rounding(), "%3% %1$.2f");
            data = chfData;
        }
    }

    ;

    /**
     * Cyprus pound
     */
    public static class CYPCurrency extends Currency {
        public CYPCurrency() {
            Data cypData = new Data("Cyprus pound", "CYP", 196, "\\xA3 C", "", 100, new Rounding(), "%3% %1$.2f");
            data = cypData;
        }
    }

    ;

    /**
     * Czech koruna
     */
    public static class CZKCurrency extends Currency {
        public CZKCurrency() {
            Data czkData = new Data("Czech koruna", "CZK", 203, "Kc", "", 100, new Rounding(), "%1$.2f %3%");
            data = czkData;
        }
    }

    ;

    /**
     * Danish krone
     */
    public static class DKKCurrency extends Currency {
        public DKKCurrency() {
            Data dkkData = new Data("Danish krone", "DKK", 208, "Dkr", "", 100, new Rounding(), "%3% %1$.2f");
            data = dkkData;
        }
    }

    ;

    /**
     * Estonian kroon
     */
    public static class EEKCurrency extends Currency {
        public EEKCurrency() {
            Data eekData = new Data("Estonian kroon", "EEK", 233, "KR", "", 100, new Rounding(), "%1$.2f %2%");
            data = eekData;
        }
    }

    ;

    /**
     * European Euro
     */
    public static class EURCurrency extends Currency {
        public EURCurrency() {
            Data eurData = new Data("European Euro", "EUR", 978, "", "", 100, new Rounding.ClosestRounding(2), "%2% %1$.2f");
            data = eurData;
        }
    }

    ;

    /**
     * British pound sterling
     */
    public static class GBPCurrency extends Currency {
        public GBPCurrency() {
            Data gbpData = new Data("British pound sterling", "GBP", 826, "\\xA3", "p", 100, new Rounding(), "%3% %1$.2f");
            data = gbpData;
        }
    }

    ;

    /**
     * Hungarian forint
     */
    public static class HUFCurrency extends Currency {
        public HUFCurrency() {
            Data hufData = new Data("Hungarian forint", "HUF", 348, "Ft", "", 1, new Rounding(), "%1$.0f %3%");
            data = hufData;
        }
    }

    ;

    /**
     * Icelandic kron
     */
    public static class ISKCurrency extends Currency {
        public ISKCurrency() {
            Data iskData = new Data("Iceland krona", "ISK", 352, "IKr", "", 100, new Rounding(), "%1$.2f %3%");
            data = iskData;
        }
    }

    ;

    /**
     * Lithuanian litas
     */
    public static class LTLCurrency extends Currency {
        public LTLCurrency() {
            Data ltlData = new Data("Lithuanian litas", "LTL", 440, "Lt", "", 100, new Rounding(), "%1$.2f %3%");
            data = ltlData;
        }
    }

    ;

    /**
     * Latvian lat
     */
    public static class LVLCurrency extends Currency {
        public LVLCurrency() {
            Data lvlData = new Data("Latvian lat", "LVL", 428, "Ls", "", 100, new Rounding(), "%3% %1$.2f");
            data = lvlData;
        }
    }

    ;

    /**
     * Maltese lira
     */
    public static class MTLCurrency extends Currency {
        public MTLCurrency() {
            Data mtlData = new Data("Maltese lira", "MTL", 470, "Lm", "", 100, new Rounding(), "%3% %1$.2f");
            data = mtlData;
        }
    }

    ;

    /**
     * Norwegian krone
     */
    public static class NOKCurrency extends Currency {
        public NOKCurrency() {
            Data nokData = new Data("Norwegian krone", "NOK", 578, "NKr", "", 100, new Rounding(), "%3% %1$.2f");
            data = nokData;
        }
    }


    /**
     * Polish zloty
     */
    public static class PLNCurrency extends Currency {
        public PLNCurrency() {
            Data plnData = new Data("Polish zloty", "PLN", 985, "zl", "", 100, new Rounding(), "%1$.2f %3%");
            data = plnData;
        }
    }


    /**
     * Romanian leu
     */
    public static class ROLCurrency extends Currency {
        public ROLCurrency() {
            Data rolData = new Data("Romanian leu", "ROL", 642, "L", "", 100, new Rounding(), "%1$.2f %3%");
            data = rolData;
        }
    }


    /**
     * Romanian new leu
     */
    public static class RONCurrency extends Currency {
        public RONCurrency() {
            Data ronData = new Data("Romanian new leu", "RON", 946, "L", "", 100, new Rounding(), "%1$.2f %3%");
            data = ronData;
        }
    }

    /**
     * Swedish krona
     */
    public static class SEKCurrency extends Currency {
        public SEKCurrency() {
            Data sekData = new Data("Swedish krona", "SEK", 752, "kr", "", 100, new Rounding(), "%1$.2f %3%");
            data = sekData;
        }
    }


    /**
     * Slovenian tolar
     */
    public static class SITCurrency extends Currency {
        public SITCurrency() {
            Data sitData = new Data("Slovenian tolar", "SIT", 705, "SlT", "", 100, new Rounding(), "%1$.2f %3%");
            data = sitData;
        }
    }


    /**
     * Slovak koruna
     */
    public static class SKKCurrency extends Currency {
        public SKKCurrency() {
            Data skkData = new Data("Slovak koruna", "SKK", 703, "Sk", "", 100, new Rounding(), "%1$.2f %3%");
            data = skkData;
        }
    }

    /**
     * Turkish lira
     */
    public static class TRLCurrency extends Currency {
        public TRLCurrency() {
            Data trlData = new Data("Turkish lira", "TRL", 792, "TL", "", 100, new Rounding(), "%1$.0f %3%");
            data = trlData;
        }
    }


    /**
     * New Turkish lira
     */
    public static class TRYCurrency extends Currency {
        public TRYCurrency() {
            Data tryData = new Data("New Turkish lira", "TRY", 949, "YTL", "", 100, new Rounding(), "%1$.2f %3%");
            data = tryData;
        }
    }

    /**
     * Austrian shilling
     */
    public static class ATSCurrency extends Currency {
        public ATSCurrency() {
            Data atsData = new Data("Austrian shilling", "ATS", 40, "", "", 100, new Rounding(), "%2% %1$.2f", new EURCurrency());
            data = atsData;
        }
    }


    /**
     * Belgian franc
     */
    public static class BEFCurrency extends Currency {
        public BEFCurrency() {
            Data befData = new Data("Belgian franc", "BEF", 56, "", "", 1, new Rounding(), "%2% %1$.0f", new EURCurrency());
            data = befData;
        }
    }


    /**
     * Deutsche mark
     */
    public static class DEMCurrency extends Currency {
        public DEMCurrency() {
            Data demData = new Data("Deutsche mark", "DEM", 276, "DM", "", 100, new Rounding(), "%1$.2f %3%", new EURCurrency());
            data = demData;
        }
    }


    /**
     * Spanish peseta
     */
    public static class ESPCurrency extends Currency {
        public ESPCurrency() {
            Data espData = new Data("Spanish peseta", "ESP", 724, "Pta", "", 100, new Rounding(), "%1$.0f %3%", new EURCurrency());
            data = espData;
        }
    }


    /**
     * Finnish markka
     */
    public static class FIMCurrency extends Currency {
        public FIMCurrency() {
            Data fimData = new Data("Finnish markka", "FIM", 246, "mk", "", 100, new Rounding(), "%1$.2f %3%", new EURCurrency());
            data = fimData;
        }
    }

    /**
     * French franc
     */
    public static class FRFCurrency extends Currency {
        public FRFCurrency() {
            Data frfData = new Data("French franc", "FRF", 250, "", "", 100, new Rounding(), "%1$.2f %2%", new EURCurrency());
            data = frfData;
        }
    }


    /**
     * Greek drachma
     */
    public static class GRDCurrency extends Currency {
        public GRDCurrency() {
            Data grdData = new Data("Greek drachma", "GRD", 300, "", "", 100, new Rounding(), "%1$.2f %2%", new EURCurrency());
            data = grdData;
        }
    }


    /**
     * Irish punt
     */
    public static class IEPCurrency extends Currency {
        public IEPCurrency() {
            Data iepData = new Data("Irish punt", "IEP", 372, "", "", 100, new Rounding(), "%2% %1$.2f", new EURCurrency());
            data = iepData;
        }
    }

    /**
     * Italian lira
     */
    public static class ITLCurrency extends Currency {
        public ITLCurrency() {
            Data itlData = new Data("Italian lira", "ITL", 380, "L", "", 1, new Rounding(), "%3% %1$.0f", new EURCurrency());
            data = itlData;
        }
    }


    /**
     * Luxembourg franc
     */
    public static class LUFCurrency extends Currency {
        public LUFCurrency() {
            Data lufData = new Data("Luxembourg franc", "LUF", 442, "F", "", 100, new Rounding(), "%1$.0f %3%", new EURCurrency());
            data = lufData;
        }
    }


    /**
     * Dutch guilder
     */
    public static class NLGCurrency extends Currency {
        public NLGCurrency() {
            Data nlgData = new Data("Dutch guilder", "NLG", 528, "f", "", 100, new Rounding(), "%3% %1$.2f", new EURCurrency());
            data = nlgData;
        }
    }


    /**
     * Portuguese escudo
     */
    public static class PTECurrency extends Currency {
        public PTECurrency() {
            Data pteData = new Data("Portuguese escudo", "PTE", 620, "Esc", "", 100, new Rounding(), "%1$.0f %3%",
                    new EURCurrency());
            data = pteData;
        }
    }

}
