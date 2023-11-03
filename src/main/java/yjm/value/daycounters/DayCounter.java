package yjm.value.daycounters;

import yjm.value.time.Date;

/**
 * 日算惯例类，计算年化时间
 **/

public class DayCounter {


    protected daycountImplement daycountImplement;

    public boolean empty() {
        return daycountImplement == null;
    }

    public String name() {
        return daycountImplement.name();
    }

    public long daycount(final Date dateStart, final Date dateEnd) {
        return daycountImplement.daycount(dateStart, dateEnd);
    }

    public double yearFraction(final Date dateStart, final Date dateEnd) {
        return yearFraction(dateStart, dateEnd, null, null);
    }

    public double yearFraction(final Date dateStart, final Date dateEnd, final Date refPeriodStart, final Date refPeriodEnd) {
        return yearFraction(dateStart, dateEnd, refPeriodStart, refPeriodEnd);
    }


    protected abstract class daycountImplement {

        protected abstract String name();

        protected abstract double yearFraction(final Date dateStart, final Date dateEnd,
                                               final Date refPeriodStart, final Date refPeriodEnd);

        protected long daycount(final Date dateStart, final Date dateEnd) {
            return dateEnd.sub(dateStart);
        }


    }

}
