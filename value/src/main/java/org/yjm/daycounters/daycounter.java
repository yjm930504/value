package org.yjm.daycounters;

import org.yjm.time.Date;

/**
 * @author Jiaming Yan
 * @description: 根据不同的日算惯例,daycount()返回两个日期间的天数,yearFraction()计算年化时间, name()返回日算惯例名称
 *
 **/

public class daycounter {

    protected daycountImplement daycountImplement;

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