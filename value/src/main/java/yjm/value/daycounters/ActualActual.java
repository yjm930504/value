package yjm.value.daycounters;

import yjm.value.time.Date;

public class ActualActual extends daycounter {

    public ActualActual(){
        super.daycountImplement = new daycountImplement();
    }

    private final class daycountImplement extends daycounter.daycountImplement{

        @Override
        public String name() {
            return "Actual/Actual";
        }

        @Override
        public double yearFraction(final Date dateStart, final Date dateEnd,
                                   final Date refPeriodStart, final Date refPeriodEnd) {
            if(dateStart.gt(dateEnd))
                return -1.0 * yearFraction(dateEnd,dateStart,new Date(),new Date());
            if(dateStart.eq(dateEnd))
                return 0.0;

            final int y1 = dateStart.year();
            final int y2 = dateEnd.year();

            final double dib1 = Date.isLeap(dateStart.year()) ? 366.0 : 365.0;
            final double dib2 = Date.isLeap(dateEnd.year()) ? 366.0 : 365.0;

            double sum = y2 - y1 - 1;

            //加上：起始日到当年年底的年化时间
            sum += (dib1 - dateStart.dayOfYear() + 1) / dib1;

            //加上到期日当年年初到到期日的年化时间
            sum += (dateEnd.dayOfYear() - 1) / dib2;

            return sum;
        }
    }
}

