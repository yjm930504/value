package yjm.value.daycounters;

import yjm.value.time.Date;


public class Actual360 extends daycounter {

    public Actual360() {
        super.daycountImplement = new daycountImplement();
    }

    private final class daycountImplement extends daycounter.daycountImplement {

        @Override
        public String name() {
            return "Actual/360";
        }

        @Override
        public double yearFraction(final Date dateStart, final Date dateEnd,
                                   final Date refPeriodStart, final Date refPeriodEnd) {
            return daycount(dateStart, dateEnd) / 360.0;
        }
    }
}
