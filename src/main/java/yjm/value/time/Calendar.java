package yjm.value.time;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author  Jiaming Yan
 * @Description 日历类，提供功能包括根据日历判断某一天是否为节假日（包括周末），是否为工作日以及给定日历下根据工作日调整规则返回工作日
 */
public class Calendar {


    private static Logger logger = LoggerFactory.getLogger(Calendar.class);
    public static final String UNKNOWN_MARKET = "未知的市场";
    public static final String UKNOWN_BUSINESS_DAY_CONVENTION = "未知的工作日调整规则";
    protected Impl impl;
    public Calendar() {}

    /**
     * @Author  Jiaming Yan
     * @Description 判断impl对象是否为空
     */
    public boolean empty() {return impl == null;}

    /**
     * @Author  Jiaming Yan
     * @Description 返回impl名称
     */
    public String name() {return impl.name();}

    /**
     * @Author  Jiaming Yan
     * @Description 根据Calendar判断是否日期为工作日
     */
    public boolean isBusinessDay(final Date d) {
        if (impl.addedHolidays.contains(d))
            return false;
        if (impl.removedHolidays.contains(d))
            return true;
        return impl.isBusinessDay(d);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 根据Calendar判断日期是否为节假日
     */
    public boolean isHoliday(final Date d) {
        return !isBusinessDay(d);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 根据Calendar判断Weekday是否为Weekend
     */
    public boolean isWeekend(final Weekday w) {
        return impl.isWeekend(w);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 根据Calendar判断日期是否为当月最后一个工作日
     */
    public boolean isEndOfMonth(final Date d) {
        return (d.month() != adjust(d.add(1)).month());
    }

    /**
     * @Author  Jiaming Yan
     * @Description 根据Calendar返回日期当月的最后一天
     */
    public Date endOfMonth(final Date d) {
        return adjust(Date.endOfMonth(d), BusinessDayConvention.Preceding);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 根据Calendar添加日期到节假日集合
     */
    public void addHoliday(final Date d) {
        //如果d是之前删除的节假，恢复更改
        impl.removedHolidays.remove(d);
        if (impl.isBusinessDay(d)) {
            impl.addedHolidays.add(d);
        }
    }

    /**
     * @Author  Jiaming Yan
     * @Description 根据Calendar从节假日集合中移除日期
     */
    public void removeHoliday(final Date d) {
        // 如果日期是人工增加的非常规节假日，恢复更改
        impl.addedHolidays.remove(d);
        if (!impl.isBusinessDay(d)) {
            impl.removedHolidays.add(d);
        }
    }

    /**
     * @Author  Jiaming Yan
     * @Description 根据Calendar返回两个日期之间的节假日
     */
    public static List<Date> holidayList(final Calendar c, final Date from, final Date to, final boolean includeWeekEnds) {

        final List<Date> result = new ArrayList<Date>();
        for (Date d = from.clone(); d.le(to); d = d.add(1)) {
            // c.isHoliday(d)：判断当前Calendar下日期d是否为节假日
            // includeWeekEnds：如果包括节假日则为True，不包括为False
            // c.isWeekend(d.weekday())：判断当前Calendar下日期d的是否为周末
            if (c.isHoliday(d) && (includeWeekEnds || !c.isWeekend(d.weekday()))) {
                result.add(d);
            }
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 根据给定的工作日调整规则，将非营业日调整为最近营业日
     */
    public Date adjust(final Date date)  {
        return adjust(date, BusinessDayConvention.Following);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 根据给定的工作日调整规则，将非营业日调整为最近营业日
     */
    public Date adjust(final Date d, final BusinessDayConvention c) {

        if (c == BusinessDayConvention.Unadjusted)
            return d.clone();

        final Date d1 = d.clone();

        if (c == BusinessDayConvention.Following || c == BusinessDayConvention.ModifiedFollowing) {

            // Following和ModifiedFollowing，向后调整一天，知道不是节假日
            while (isHoliday(d1)) {d1.inc();}
            if (c == BusinessDayConvention.ModifiedFollowing) {
                //如果是ModifiedFollowing，判断调整后的日期是否进入下一个月
                if (d1.month() != d.month())
                    //如果到了下一个月，日期d按照Preceding调整
                    return adjust(d, BusinessDayConvention.Preceding);
            }
        } else if (c == BusinessDayConvention.Preceding || c == BusinessDayConvention.ModifiedPreceding) {
            // Preceding和ModifiedPreceding，每次向前调整一天，直到不是节假日
            while (isHoliday(d1)) {d1.dec();}
            //ModifiedPreceding 进一步判断调整后日期是否进入下一个月
            if (c == BusinessDayConvention.ModifiedPreceding && d1.month() != d.month())
                //如果到了下一个月，按照Following调整日期d
                return adjust(d, BusinessDayConvention.Following);
        } else
            throw new RuntimeException(UKNOWN_BUSINESS_DAY_CONVENTION);

        return d1;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 根据给定的工作日调整规则，返回日期date调整Period后的工作日
     */
    public Date advance(final Date date, final Period period, final BusinessDayConvention convention) /* @ReadOnly */ {
        return advance(date, period, convention, false);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 根据给定的工作日调整规则，返回日期date调整Period后的工作日
     */
    public Date advance(
            final Date date,
            final Period period,
            final BusinessDayConvention convention,
            final boolean endOfMonth) {
        return advance(date, period.length(), period.units(), convention, endOfMonth);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 按照Following规则，返回日期date调整n个unit后的工作日
     */
    public Date advance(
            final Date date,
            final int n,
            final TimeUnit unit) {
        return advance(date, n, unit, BusinessDayConvention.Following, false);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 按照Following规则，返回日期date调整Period后的工作日
     */
    public Date advance(final Date date, final Period period)  {
        return advance(date, period, BusinessDayConvention.Following, false);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 根据给定的工作日调整规则，返回日期d调整n个unit后的工作日
     * 对于Days的处理：advance(2023-06-16,3,Days,Following,false)，按照Following返回2023年6月16日(周五)后的第三个工作日，返回2023年6月21日(周三)，而不是2023年6月19日(周一)，跳过了节假日
     * 对于Weeks、Months、Years，是先加上Period之后，再调整为最近的工作日
     * Months和Years需要考虑endofMonth
     */
    public Date advance(
            final Date d,
            int n,
            final TimeUnit unit,
            final BusinessDayConvention c,
            final boolean endOfMonth) {

        //Period的长度为0，根据工作日调整规则调整日期
        if (n == 0)
            return adjust(d, c);
        //Period的单位为天，调整工作日（不算节假日）
        else if (unit == TimeUnit.Days) {
            final Date d1 = d.clone();
            if (n > 0) {
                while (n > 0) {
                    d1.inc();
                    while (isHoliday(d1)) {
                        d1.inc();
                    }
                    n--;
                }
            } else {
                while (n < 0) {
                    d1.dec();
                    while (isHoliday(d1)) {
                        d1.dec();
                    }
                    n++;
                }
            }
            return d1;
        } else if (unit == TimeUnit.Weeks) {
            final Date d1 = d.add(new Period(n, unit));
            return adjust(d1, c);
        } else {
            final Date d1 = d.add(new Period(n, unit));

            //例如：advance(2023-03-31,1,Months,Following,True), d1 = 2023-04-30(周日)
            //如果 endOfMonth = true,则返回 d1的月末 = 2023-04-30(周日)
            //如果 endOfMonth = false,则返回 adjust(2023-04-30, Following) = 2023-5-01(周一)
            if (endOfMonth && isEndOfMonth(d))
                return endOfMonth(d1);

            return adjust(d1, c);
        }
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回两个日期间的工作日天数，算头不算尾
     */
    public int businessDaysBetween(final Date from, final Date to) {
        return businessDaysBetween(from, to, true, false);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回两个日期间的工作日天数
     * @param from 起始日
     * @param to 接受日
     * @param includeFirst 是否包含第一天
     * @param includeLast 是否包含最后一天
     */
    public int businessDaysBetween(final Date from, final Date to,
                                   final boolean includeFirst, final boolean includeLast) {
        int wd = 0;
        if (from.ne(to)) {
            if (from.lt(to)) {
                for (Date d = from.clone(); d.lt(to); d = d.add(1)) {
                    if (isBusinessDay(d)) {
                        ++wd;
                    }
                }
                if (isBusinessDay(to)) {
                    ++wd;
                }
            } else if (from.gt(to)) {
                for (Date d = to.clone(); d.lt(from); d = d.add(1)) {
                    if (isBusinessDay(d)) {
                        ++wd;
                    }
                }
                if (isBusinessDay(from)) {
                    ++wd;
                }
            }
            // 如果不包含第一天，但第一天为工作日，则减一天
            if (isBusinessDay(from) && !includeFirst) {
                wd--;
            }
            // 如果不包含最后一天，但最后一天为工作日，则减一天
            if (isBusinessDay(to) && !includeLast) {
                wd--;
            }

            //如果起始日大于结束日，返回负数
            if (from.gt(to)) {
                wd = -wd;
            }
        }

        return wd;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 判断两个Calendar是否相同
     */
    public static boolean eq(final Calendar c1, final Calendar c2) {
        return (c1.empty() && c2.empty()) || (!c1.empty() && !c2.empty() && c1.name().equals(c2.name()));
    }

    /**
     * @Author  Jiaming Yan
     * @Description 判断两个Calendar是否不相同
     */
    public static boolean ne(final Calendar c1, final Calendar c2) {
        return !eq(c1, c2);
    }


    protected abstract class Impl {

        /**
         * @Author  Jiaming Yan
         * @Description 存储非常规节假日（非周末的节假日）
         */
        private final Set<Date> addedHolidays = new HashSet<Date>();

        /**
         * @Author  Jiaming Yan
         * @Description 存储非常规工作日（周末变更为节假日）
         */
        private final Set<Date> removedHolidays = new HashSet<Date>();

        protected Impl() {}

        /**
         * @Author  Jiaming Yan
         * @Description 返还impl名称
         */
        public abstract String name();

        /**
         * @Author  Jiaming Yan
         * @Description 判断日期是否为工作日
         */
        public abstract boolean isBusinessDay(final Date d);

        /**
         * @Author  Jiaming Yan
         * @Description 判断Weekday是否为节假日
         */
        public abstract boolean isWeekend(Weekday w);

    }

}


