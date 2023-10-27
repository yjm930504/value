/*
 Copyright (C) 2008 Richard Gomes

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

package yjm.value.quotes;

import yjm.value.QL;
import yjm.value.math.Constants;


public class SimpleQuote extends Quote {

    private double value;

    public SimpleQuote() {
        this(Constants.NULL_REAL);
    }

    public SimpleQuote(final SimpleQuote o) {
        this.value = o.value;
    }

    public SimpleQuote(final double d) {
        this.value = d;
    }

    public double setValue() {
        return setValue(Constants.NULL_REAL);
    }

    /**
     * 返回差异
     */
    public double setValue(final double value) {
        final double diff = this.value - value;
        if (diff != 0.0) {
            this.value = value;
            notifyObservers();
        }
        return diff;
    }

    public void reset() {
        setValue(Constants.NULL_REAL);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public final double value() {
        QL.require(isValid(), "invalid simple quote: no value available");
        return value;
    }

    @Override
    public boolean isValid() {
        return !Double.isNaN(value);
    }

}
