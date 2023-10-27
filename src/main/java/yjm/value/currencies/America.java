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

public class America {

    /**
     * Argentinian peso
     */
    public static class ARSCurrency extends Currency {
        public ARSCurrency() {
            Data arsData = new Data(
                    "Argentinian peso",
                    "ARS",
                    32,
                    "",
                    "",
                    100,
                    new Rounding(),
                    "%2% %1$.2f");
            data = arsData;
        }
    }

    /**
     * Brazilian real
     */
    public static class BRLCurrency extends Currency {
        public BRLCurrency() {
            Data brlData = new Data(
                    "Brazilian real",
                    "BRL",
                    986,
                    "R$",
                    "",
                    100,
                    new Rounding(),
                    "%3% %1$.2f");
            data = brlData;
        }
    }

    /**
     * Canadian dollar
     */
    public static class CADCurrency extends Currency {
        public CADCurrency() {
            Data cadData = new Data(
                    "Canadian dollar",
                    "CAD",
                    124,
                    "Can$",
                    "",
                    100,
                    new Rounding(),
                    "%3% %1$.2f");
            data = cadData;
        }
    }

    /**
     * Chilean peso
     */
    public static class CLPCurrency extends Currency {
        public CLPCurrency() {
            Data clpData = new Data("Chilean peso", "CLP", 152, "Ch$", "", 100, new Rounding(), "%3% %1$.0f");
            data = clpData;
        }
    }


    /**
     * Colombian peso
     */
    public static class COPCurrency extends Currency {
        public COPCurrency() {
            Data copData = new Data(
                    "Colombian peso",
                    "COP", 170,
                    "Col$",
                    "",
                    100,
                    new Rounding(),
                    "%3% %1$.2f");
            data = copData;
        }
    }

    /**
     * Mexican peso
     */
    public static class MXNCurrency extends Currency {
        public MXNCurrency() {
            Data mxnData = new Data(
                    "Mexican peso",
                    "MXN",
                    484,
                    "Mex$",
                    "",
                    100,
                    new Rounding(),
                    "%3% %1$.2f");
            data = mxnData;
        }
    }


    /**
     * Peruvian nuevo
     */
    public static class PENCurrency extends Currency {
        public PENCurrency() {
            Data penData = new Data(
                    "Peruvian nuevo sol",
                    "PEN",
                    604,
                    "S/.",
                    "",
                    100,
                    new Rounding(),
                    "%3% %1$.2f");
            data = penData;
        }
    }

    /**
     * Peruvian inti
     */
    public static class PEICurrency extends Currency {
        public PEICurrency() {
            Data peiData = new Data(
                    "Peruvian inti",
                    "PEI", 998,
                    "I/.", "",
                    100, new Rounding(),
                    "%3% %1$.2f");
            data = peiData;
        }
    }

    /**
     * Peruvian sol
     */
    public static class PEHCurrency extends Currency {
        public PEHCurrency() {
            Data pehData = new Data(
                    "Peruvian sol",
                    "PEH",
                    999,
                    "S./",
                    "",
                    100,
                    new Rounding(),
                    "%3% %1$.2f");
            data = pehData;
        }
    }

    /**
     * Trinidad & Tobago dollar
     */
    public static class TTDCurrency extends Currency {
        public TTDCurrency() {
            Data ttdData = new Data(
                    "Trinidad & Tobago dollar",
                    "TTD", 780,
                    "TT$", "",
                    100, new Rounding(),
                    "%3% %1$.2f");
            data = ttdData;
        }
    }

    ;

    /**
     * U.S. dollar
     */
    public static class USDCurrency extends Currency {
        public USDCurrency() {
            Data usdData = new Data(
                    "U.S. dollar",
                    "USD", 840,
                    "$", "\u00A2",
                    100,
                    new Rounding(),
                    "%3% %1$.2f");
            data = usdData;
        }
    }

    ;

    /**
     * Venezuelan bolivar
     */
    public static class VEBCurrency extends Currency {
        public VEBCurrency() {
            Data vebData = new Data(
                    "Venezuelan bolivar",
                    "VEB",
                    862,
                    "Bs",
                    "",
                    100,
                    new Rounding(),
                    "%3% %1$.2f");
            data = vebData;
        }
    }

}
