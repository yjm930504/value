package yjm.value.daycounters;

import yjm.value.time.Date;

public class Actual365Fixed extends DayCounter {

    public Actual365Fixed() {
        super.daycountImplement = new daycountImplement();
    }

    private final class daycountImplement extends DayCounter.daycountImplement{

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
