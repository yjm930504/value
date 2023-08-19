package org.yjm.daycounters;

import org.yjm.time.Date;
public class Actual365F extends daycounter {

    public Actual365F() {
        super.daycountImplement = new daycountImplement();
    }

    private final class daycountImplement extends daycounter.daycountImplement{

        @Override
        public String name() {
            return "ACT/365(Fixed)";
        }

        @Override
        public double yearFraction(final Date dateStart, final Date dateEnd,
                                   final Date refPeriodStart, final Date refPeriodEnd) {
            return daycount(dateStart, dateEnd) / 365.0;
        }

    }
}
