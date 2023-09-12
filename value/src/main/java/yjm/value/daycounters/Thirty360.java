package yjm.value.daycounters;

import yjm.value.time.Date;

public class Thirty360 extends daycounter {

    public Thirty360() {
        super.daycountImplement = new daycountImplement();
    }


    private final class daycountImplement extends daycounter.daycountImplement {
        @Override
        protected String name() {
            return "30/360";
        }

        protected long daycount(final Date d1, final Date d2){

            final int y1 = d1.year();
            final int y2 = d2.year();

            final int dd1 = d1.dayOfMonth();
            final int dd2 = d2.dayOfMonth();

            final int m1 = d1.month().value();
            final int m2 = d2.month().value();

            return 360 * (y2-y1) + 30 * (m2-m1) + (dd2-dd1);


        }
        @Override
        protected double yearFraction(Date dateStart, Date dateEnd, Date refPeriodStart, Date refPeriodEnd) {
            return daycount(dateStart,dateEnd) / 360.0;
        }
    }
}
