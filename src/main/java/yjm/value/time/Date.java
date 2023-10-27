package yjm.value.time;

import yjm.value.lang.exceptions.LibraryException;
import yjm.value.util.DefaultObservable;
import yjm.value.util.Observable;
import yjm.value.util.Observer;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;


/**
 * 日期类，serialNumber表示日期到1900年1月1日的天数
 */

public class Date implements Observable,Comparable<Date>, Serializable , Cloneable{

    private static final long serialVersionUID = -7150540867519744332L;

    // 非闰年月份长度
    private static final int[] monthLength = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    // 闰年月份长度
    private static final int[] monthLeapLength = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    // 非闰年按月累计时间长度
    private static final int[] monthOffset = {
            0, 31, 59, 90, 120, 151,        // Jan - Jun
            181, 212, 243, 273, 304, 334,   // Jun - Dec
            365
    };

    // 闰年按月累计时间长度
    private static final int[] monthLeapOffset = {
            0, 31, 60, 91, 121, 152,        // Jan - Jun
            182, 213, 244, 274, 305, 335,   // Jun - Dec
            366
    };

    /**
     * 从1990年开始，每年12月31日的累计天数。例如：yearOffset[2]表示1901年的12月31日距离1900年1月1日有731天
     */
    private static final int[] yearOffset = {
            // 1900-1909
            0, 366, 731, 1096, 1461, 1827, 2192, 2557, 2922, 3288,
            // 1910-1919
            3653, 4018, 4383, 4749, 5114, 5479, 5844, 6210, 6575, 6940,
            // 1920-1929
            7305, 7671, 8036, 8401, 8766, 9132, 9497, 9862, 10227, 10593,
            // 1930-1939
            10958, 11323, 11688, 12054, 12419, 12784, 13149, 13515, 13880, 14245,
            // 1940-1949
            14610, 14976, 15341, 15706, 16071, 16437, 16802, 17167, 17532, 17898,
            // 1950-1959
            18263, 18628, 18993, 19359, 19724, 20089, 20454, 20820, 21185, 21550,
            // 1960-1969
            21915, 22281, 22646, 23011, 23376, 23742, 24107, 24472, 24837, 25203,
            // 1970-1979
            25568, 25933, 26298, 26664, 27029, 27394, 27759, 28125, 28490, 28855,
            // 1980-1989
            29220, 29586, 29951, 30316, 30681, 31047, 31412, 31777, 32142, 32508,
            // 1990-1999
            32873, 33238, 33603, 33969, 34334, 34699, 35064, 35430, 35795, 36160,
            // 2000-2009
            36525, 36891, 37256, 37621, 37986, 38352, 38717, 39082, 39447, 39813,
            // 2010-2019
            40178, 40543, 40908, 41274, 41639, 42004, 42369, 42735, 43100, 43465,
            // 2020-2029
            43830, 44196, 44561, 44926, 45291, 45657, 46022, 46387, 46752, 47118,
            // 2030-2039
            47483, 47848, 48213, 48579, 48944, 49309, 49674, 50040, 50405, 50770,
            // 2040-2049
            51135, 51501, 51866, 52231, 52596, 52962, 53327, 53692, 54057, 54423,
            // 2050-2059
            54788, 55153, 55518, 55884, 56249, 56614, 56979, 57345, 57710, 58075,
            // 2060-2069
            58440, 58806, 59171, 59536, 59901, 60267, 60632, 60997, 61362, 61728,
            // 2070-2079
            62093, 62458, 62823, 63189, 63554, 63919, 64284, 64650, 65015, 65380,
            // 2080-2089
            65745, 66111, 66476, 66841, 67206, 67572, 67937, 68302, 68667, 69033,
            // 2090-2099
            69398, 69763, 70128, 70494, 70859, 71224, 71589, 71955, 72320, 72685,
            // 2100-2109
            73050, 73415, 73780, 74145, 74510, 74876, 75241, 75606, 75971, 76337,
            // 2110-2119
            76702, 77067, 77432, 77798, 78163, 78528, 78893, 79259, 79624, 79989,
            // 2120-2129
            80354, 80720, 81085, 81450, 81815, 82181, 82546, 82911, 83276, 83642,
            // 2130-2139
            84007, 84372, 84737, 85103, 85468, 85833, 86198, 86564, 86929, 87294,
            // 2140-2149
            87659, 88025, 88390, 88755, 89120, 89486, 89851, 90216, 90581, 90947,
            // 2150-2159
            91312, 91677, 92042, 92408, 92773, 93138, 93503, 93869, 94234, 94599,
            // 2160-2169
            94964, 95330, 95695, 96060, 96425, 96791, 97156, 97521, 97886, 98252,
            // 2170-2179
            98617, 98982, 99347, 99713, 100078, 100443, 100808, 101174, 101539, 101904,
            // 2180-2189
            102269, 102635, 103000, 103365, 103730, 104096, 104461, 104826, 105191, 105557,
            // 2190-2199
            105922, 106287, 106652, 107018, 107383, 107748, 108113, 108479, 108844, 109209,
            // 2200
            109574
    };

    /**
     * 从1900年开始，判断是否为闰年。例：yearIsLeap[4]表示1904年为闰年。
     */
    private static final boolean[] yearIsLeap = {
            // 1900-1909
            true, false, false, false, true, false, false, false, true, false,
            // 1910-1919
            false, false, true, false, false, false, true, false, false, false,
            // 1920-1929
            true, false, false, false, true, false, false, false, true, false,
            // 1930-1939
            false, false, true, false, false, false, true, false, false, false,
            // 1940-1949
            true, false, false, false, true, false, false, false, true, false,
            // 1950-1959
            false, false, true, false, false, false, true, false, false, false,
            // 1960-1969
            true, false, false, false, true, false, false, false, true, false,
            // 1970-1979
            false, false, true, false, false, false, true, false, false, false,
            // 1980-1989
            true, false, false, false, true, false, false, false, true, false,
            // 1990-1999
            false, false, true, false, false, false, true, false, false, false,
            // 2000-2009
            true, false, false, false, true, false, false, false, true, false,
            // 2010-2019
            false, false, true, false, false, false, true, false, false, false,
            // 2020-2029
            true, false, false, false, true, false, false, false, true, false,
            // 2030-2039
            false, false, true, false, false, false, true, false, false, false,
            // 2040-2049
            true, false, false, false, true, false, false, false, true, false,
            // 2050-2059
            false, false, true, false, false, false, true, false, false, false,
            // 2060-2069
            true, false, false, false, true, false, false, false, true, false,
            // 2070-2079
            false, false, true, false, false, false, true, false, false, false,
            // 2080-2089
            true, false, false, false, true, false, false, false, true, false,
            // 2090-2099
            false, false, true, false, false, false, true, false, false, false,
            // 2100-2109
            false, false, false, false, true, false, false, false, true, false,
            // 2110-2119
            false, false, true, false, false, false, true, false, false, false,
            // 2120-2129
            true, false, false, false, true, false, false, false, true, false,
            // 2130-2139
            false, false, true, false, false, false, true, false, false, false,
            // 2140-2149
            true, false, false, false, true, false, false, false, true, false,
            // 2150-2159
            false, false, true, false, false, false, true, false, false, false,
            // 2160-2169
            true, false, false, false, true, false, false, false, true, false,
            // 2170-2179
            false, false, true, false, false, false, true, false, false, false,
            // 2180-2189
            true, false, false, false, true, false, false, false, true, false,
            // 2190-2199
            false, false, true, false, false, false, true, false, false, false,
            // 2200
            false
    };

    private long serialNumber;

    /**
     * 构造函数，设置Date的serialNumber为0
     */
    public Date() {
        this(0);
    }

    /**
     * 构造函数，传入serialNumber
     * @param serialNumber
     */
    public Date(final long serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * 构造函数，传入年月日
     * @param day
     * @param month
     * @param year
     */
    public Date(final int day, final Month month, final int year) {
        this(fromDMY(day, month.value(), year));
    }

    /**
     * 构造函数，传入年月日
     * @param day
     * @param month
     * @param year
     */
    public Date(final int day, final int month, final int year) {
        this(fromDMY(day, month, year));
    }

    /**
     * 将JAVA的Date类对象转换Date类
     * @param date
     */
    public Date(final java.util.Date date) {
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        final int d = c.get(Calendar.DAY_OF_MONTH);
        final int m = c.get(Calendar.MONTH);
        final int y = c.get(Calendar.YEAR);
        this.serialNumber = fromDMY(d, m + 1, y);
    }

    /**
     * 返回当天日期
     */
    public static final Date todaysDate() {
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        final int d = cal.get(java.util.Calendar.DAY_OF_MONTH);
        final int m = cal.get(java.util.Calendar.MONTH);
        final int y = cal.get(java.util.Calendar.YEAR);
        return new Date(d, m + 1, y);
    }


    /**
     * 判断是否为闰年
     * @param year
     */
    public static final boolean isLeap(final int year) {
        return yearIsLeap[year - 1900];
    }

    /**
     * @Author  Jiaming Yan
     * @Description 输入日期，返回日期当月的的最后一日。例：输入2023年5月1日，返回2023年5月31日
     * @param d Date类对象
     */
    public static final Date endOfMonth(final Date d) {
        final int m = d.month().value();
        final int y = d.year();
        return new Date(monthLength(m, isLeap(y)), m, y);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 输入日期，判断日期是否为当月最后一日。
     * @param d Date类对象
     */
    public static final boolean isEndOfMonth(final Date d) {
        return (d.dayOfMonth() == monthLength(d.month().value(), isLeap(d.year())));
    }

    /**
     * @Author  Jiaming Yan
     * @Description 输入日期，返回日期d下一个w的日期.例：2023年5月31日为星期三，计算5月31日后的下一个星期一（w为Monday）为2023年6月5日
     * @param d 日期，Date
     * @param w 周，例：Monday
     */
    public static final Date nextWeekday(final Date d, final Weekday w) {
        final int wd = d.weekday().value();
        final int dow = w.value();
        return new Date(d.serialNumber + (wd > dow ? 7 : 0) - wd + dow);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回year年month月第nth的dayofWeek的日期。例如：nthWeekday(1,Thursday,5,2023)表示2023年5月第一个星期四是5月4日。
     * @param nth 第n个
     * @param dayOfWeek 周
     * @param month 月
     * @param year 年
     */
    public static final Date nthWeekday(final int nth, final Weekday dayOfWeek, final int month, final int year) {
        final int m = month;
        final int y = year;
        final int dow = dayOfWeek.value();
        final int first = new Date(1, m, y).weekday().value();
        final int skip = nth - (dow >= first ? 1 : 0);
        return new Date(1 + dow - first + skip * 7, m, y);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回y年m月第n个w的日期。例如：nthWeekday(1,Thursday,May,2023)表示2023年5月第一个星期四是5月4日。
     * @param n 第n个
     * @param w 周
     * @param m 月
     * @param y 年
     */
    public static final Date nthWeekday(final int n, final Weekday w, final Month m, final int y) {
        return nthWeekday(n, w, m.value(), y);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回一系列日期中的最小日期
     */
    public static Date min(final Date... t) {
        if (t.length == 0)
            return new Date();
        else {
            Date min = t[0];
            for (int i = 1; i < t.length; i++) {
                final Date curr = t[i];
                if (curr.lt(min)) {
                    min = curr;
                }
            }
            return min;
        }
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回一系列日期中的最大日期
     */
    public static Date max(final Date... t) {
        if (t.length == 0)
            return new Date();
        else {
            Date max = t[0];
            for (int i = 1; i < t.length; i++) {
                final Date curr = t[i];
                if (curr.gt(max)) {
                    max = curr;
                }
            }
            return max;
        }
    }

    /**
     * @Author  Jiaming Yan
     * @Description 最小日期offset，367，Jan 1st, 1901
     */
    private static long minimumSerialNumber() {
        return 367;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 最大日期offset，109574，Dec 31st, 2199
     */
    private static long maximumSerialNumber() {
        return 109574;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 根据年月日返回offset, 如2023年5月31日，对应45077
     * @param d 日
     * @param m 月
     * @param y 年
     */
    private static long fromDMY(final int d, final int m, final int y) {
        final boolean leap = isLeap(y);
        final int offset = monthOffset(m, leap);
        final long result = d + offset + yearOffset(y);
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 根据月份及是否为闰年,返回当月天数
     * @param m 月
     * @param leapYear 是否为闰年
     */
    private static int monthLength(final int m, final boolean leapYear) {
        return (leapYear ? monthLeapLength[m - 1] : monthLength[m - 1]);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 根据月份及是否为闰年,返回年初至当月月初的累计天数
     * @param m 月
     * @param leapYear 是否为闰年
     */
    private static int monthOffset(final int m, final boolean leapYear) {
        return (leapYear ? monthLeapOffset[m - 1] : monthOffset[m - 1]);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 根据年份,返回当年的累计天数offset
     * @param year 年
     */
    private static long yearOffset(final int year) {
        return yearOffset[year - 1900];
    }

//    /**
//     * @Author  Jiaming Yan
//     * @Description 返回从小到大的一组日期（dates）中大于等于另一个日期(value)的第一个INDEX
//     * @param dates 日期
//     * @param value 比较的日期
//     */
//    public static int lowerBound(final List<Date> dates, final Date value) {
//        int len = dates.size();
//        int from = 0;
//        int half;
//        int middle;
//
//        while (len > 0) {
//            half = len >> 1;
//            middle = from;
//            middle = middle + half;
//
//            if (value.compareTo(dates.get(middle)) == 1) {
//                from = middle;
//                from++;
//                len = len - half - 1;
//            } else {
//                len = half;
//            }
//        }
//        return from;
//    }

//    // 返回一组日期中，大于等于另一个日期的第一个INDEX
//    public static int upperBound(final List<Date> dates, final Date value) {
//
//        int len = dates.size();
//        int from = 0;
//        int half;
//        int middle;
//
//        while (len > 0) {
//            half = len >> 1;
//            middle = from;
//            middle = middle + half;
//            if (value.compareTo(dates.get(middle)) == -1) {
//                len = half;
//            } else {
//                from = middle;
//                from++;
//                len = len - half - 1;
//            }
//        }
//        return from;
//    }

    /**
     * @Author  Jiaming Yan
     * @Description 计算日期所在一周的周几，如2023年6月1日，返回Thursday
     */
    public Weekday weekday() {
        final int w = (int) (serialNumber % 7);
        return Weekday.valueOf(w == 0L ? 7 : w);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回日期所在月份的第几天，如2023年5月8日，返回8
     */
    public int dayOfMonth() {
        return dayOfYear() - monthOffset(month().value(), isLeap(year()));
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回日期的所在年份的第几天，如2023年5月8日，返回128
     */
    public int dayOfYear() {
        return (int) (serialNumber - yearOffset(year()));
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回日期的所在月份，如2023年6月1日，返回June
     */
    public Month month() {
        // d为日期的当年累计天数，如2023年5月31日，d= 151
        final int d = dayOfYear();

        // m = 151/30 + 1 = 6 : “/”整数相除向下取整
        int m = d / 30 + 1;

        // 判断日期当年是否为闰年,2023年不是闰年，leap = false
        final boolean leap = isLeap(year());

        // 151 <= mothOffset(6,false) = 151
        // m = m - 1 = 5
        while (d <= monthOffset(m, leap)) {
            --m;
        }
        while (d > monthOffset(m + 1, leap)) {
            ++m;
        }
        return Month.valueOf(m);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回日期的所在年度，如2023年6月1日，返回2023
     */
    public int year() {
        int y = (int) (serialNumber / 365) + 1900;
        if (serialNumber <= yearOffset(y)) {
            --y;
        }
        return y;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回日期的serialNumber
     */
    public long serialNumber() {
        return this.serialNumber;
    }

    /**
     * @Author  Jiaming Yan
     * @param period
     * @Description 返回日期加上Period后的日期
     */
    public Date addAssign(final Period period) {
        serialNumber = advance(this, period.length(), period.units());
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @param days
     * @Description 返回日期减去(days)天后的日期
     */
    public Date subAssign(final int days) {
        serialNumber -= days;
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @param period
     * @Description 返回日期减去Period后的日期
     */
    public Date subAssign(final Period period) {
        serialNumber = advance(this, -1 * period.length(), period.units());
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回日期加上一天后的日期
     */
    public Date inc() {
        serialNumber++;
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回日期加减去一天后的日期
     */
    public Date dec() {
        serialNumber--;
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回日期加上(days)天后的日期对象
     */
    public Date add(final int days) {
        return new Date(this.serialNumber + days);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回日期加上Period后的日期对象
     */
    public Date add(final Period period) {
        return new Date(advance(this, period.length(), period.units()));
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回日期减去(days)天后的日期对象
     */
    public Date sub(final int days) {
        return new Date(this.serialNumber - days);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回日期减去Period后的日期对象
     */
    public Date sub(final Period period) {
        return new Date(advance(this, -1 * period.length(), period.units()));
    }

    /**
     * @Author  Jiaming Yan
     * @param another 用于减去的日期
     * @Description 返回两个日期间的天数
     */
    public long sub(final Date another) {
        return serialNumber - another.serialNumber;
    }

    /**
     * @Author  Jiaming Yan
     * @param another 用于比较的日期
     * @Description 判断两个日期是否相同
     */
    public boolean eq(final Date another) {
        return serialNumber == another.serialNumber;
    }

    /**
     * @Author  Jiaming Yan
     * @param another 用于比较的日期
     * @Description 判断两个日期是否不同
     */
    public boolean ne(final Date another) {
        return serialNumber != another.serialNumber;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 判断日期是否小于another
     */
    public boolean lt(final Date another) {
        return serialNumber < another.serialNumber;
    }


    /**
     * @Author  Jiaming Yan
     * @Description 判断日期是否小于等于another
     */
    public boolean le(final Date another) {
        return serialNumber <= another.serialNumber;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 判断日期是否大于another
     */
    public boolean gt(final Date another) {
        return serialNumber > another.serialNumber;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 判断日期是否大于等于another
     */
    public boolean ge(final Date another) {
        return serialNumber >= another.serialNumber;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 判断日期是否在1900年1月1日之前
     */
    public boolean isNull() {
        return this.serialNumber <= 0;
    }


    /**
     * @Author  Jiaming Yan
     * @Description 判断日期是否为当天
     */
    public final boolean isToday() {
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        final int d = cal.get(java.util.Calendar.DAY_OF_MONTH);
        final int m = cal.get(java.util.Calendar.MONTH);
        final int y = cal.get(java.util.Calendar.YEAR);
        return serialNumber == fromDMY(d, m + 1, y);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回util Date类的日期对象
     */
    public java.util.Date longDate() {
        return new LongDate();
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回util Date类的日期对象
     */
    public java.util.Date shortDate() {
        return new ShortDate();
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回util Date类的日期对象
     */
    public java.util.Date isoDate() {
        return new ISODate();
    }


    /**
     * @Author  Jiaming Yan
     * @param o
     * @Description 比较两个日期，日期相同返回0，日期小于o返回-1，大于o返回1
     */
    @Override
    //比较日期，如果日期相等返回0，如果日期小于o，返回-1，大于返回1
    public int compareTo(final Date o) {
        if (this.equals(o))
            return 0;
        if (this.le(o))
            return -1;
        return 1;
    }


    @Override
    public int hashCode() {
        return (int) this.serialNumber;
    }

    /**
     * @Author  Jiaming Yan
     * @param anObject
     * @Description 比较日期对象是否相同，相同返回true,不相同返回falses
     */
    public boolean equals(final Object anObject) {
        if (this == anObject)
            return true;
        if (anObject == null)
            return false;
        return anObject instanceof Date &&
                ((Date) anObject).fEquals(this);
    }

    /**
     * @Author  Jiaming Yan
     * @param other
     * @Description 比较日期是否，相同返回true,不相同返回falses
     */
    protected boolean fEquals(final Date other) {
        return eq(other);
    }

    @Override
    public String toString() {
        return longDate().toString();
    }

    /**
     * @Author  Jiaming Yan
     * @Description
     * @param units 时间单位
     * @param date  日期
     * @param n 时间单位数量
     * @return 返回加上n个units的serialNumber
     */
    private long advance(final Date date, final int n, final TimeUnit units) {
        switch (units) {
            case Days:
                return (n + date.serialNumber);
            case Weeks:
                return (7L * n + date.serialNumber);
            case Months: {
                int d = date.dayOfMonth();
                int m = date.month().value() + n;
                int y = date.year();
                //超过12月加1年
                while (m > 12) {
                    m -= 12;
                    y += 1;
                }
                //小于1月减一年
                while (m < 1) {
                    m += 12;
                    y -= 1;
                }
                final int length = monthLength(m, isLeap(y));
                //2023年3月31日减一个月应该是2023年2月28日
                if (d > length) {
                    d = length;
                }
                final long result = fromDMY(d, m, y);
                return result;
            }
            case Years: {
                int d = date.dayOfMonth();
                final int m = date.month().value();
                final int y = date.year() + n;

                if (d == 29 && m == Month.February.value() && !isLeap(y)) {
                    d = 28;
                }
                final long result = fromDMY(d, m, y);
                return result;
            }
        }
        return this.serialNumber;
    }



    private final Observable delegatedObservable = new DefaultObservable(this);
    @Override
    public void addObserver(Observer observer) {
        delegatedObservable.addObserver(observer);
    }

    @Override
    public int countObservers() {
        return 0;
    }

    @Override
    public List<Observer> getObservers() {
        return null;
    }

    @Override
    public void deleteObserver(Observer observer) {

    }

    @Override
    public void deleteObservers() {

    }

    @Override
    public void notifyObservers() {

    }

    @Override
    public void notifyObservers(Object arg) {

    }

    /**
     * @Author  Jiaming Yan
     * @Description 将Date类的对象变为util.Date类对象，通过toString()方法输出如下日期格式：September 18, 2009
     */
    private final class LongDate extends java.util.Date {

        private static final long serialVersionUID = -8382775848256835100L;

        private LongDate() {
            super((serialNumber - 25569) * 86400000L);
        }

        @Override
        public String toString() {
            if (isNull())
                return "日期为空";
            else {
                final StringBuilder sb = new StringBuilder();
                final Formatter formatter = new Formatter(sb, Locale.US);
                formatter.format("%s %d, %d", month(), dayOfMonth(), year());
                return sb.toString();
            }
        }
    }

    /**
     * @Author  Jiaming Yan
     * @Description 将Date类的对象变为util.Date类对象，通过toString()方法输出如下日期格式：09/18/2009
     */
    private final class ShortDate extends java.util.Date {

        private static final long serialVersionUID = -4372510060405020533L;

        private ShortDate() {
            super((serialNumber - 25569) * 86400000L);
        }

        @Override
        public String toString() {
            if (isNull())
                return "日期为空";
            else {
                final StringBuilder sb = new StringBuilder();
                final Formatter formatter = new Formatter(sb, Locale.US);
                formatter.format("%02d/%02d/%4d", month().value(), dayOfMonth(), year());
                return sb.toString();
            }
        }
    }

    /**
     * @Author  Jiaming Yan
     * @Description 将Date类的对象变为util.Date类对象，通过toString()方法输出如下日期格式：2009-09-18
     */
    private final class ISODate extends java.util.Date {

        private static final long serialVersionUID = 4824909887446169897L;

        private ISODate() {
            super((serialNumber - 25569) * 86400000L);
        }

        @Override
        public String toString() {
            if (isNull())
                return "日期为空";
            else {
                final StringBuilder sb = new StringBuilder();
                final Formatter formatter = new Formatter(sb, Locale.US);
                final Calendar c = Calendar.getInstance();
                c.setTime(this);
                formatter.format("%04d-%02d-%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
                return sb.toString();
            }
        }
    }

    @Override
    public Date clone() {
        try {
            return (Date) super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new LibraryException(e);
        }
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回有序的日期序列中，第一个大于或等于value的index
     */
    public static int lowerBound(final List<Date> dates, final Date value) {

        //二分法
        int len = dates.size();
        int from = 0;
        int half;
        int middle;

        while (len > 0) {
            half = len >> 1;
            middle = from;
            middle = middle + half;

            //value大于dates的中间日期
            if (value.compareTo(dates.get(middle)) == 1) {
                from = middle;
                from++;
                len = len - half - 1;
            } else {
                len = half;
            }
        }
        return from;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回有序的日期序列中，第一个大于value的index
     */
    public static int upperBound(final List<Date> dates, final Date value) {

        int len = dates.size();
        int from = 0;
        int half;
        int middle;

        while (len > 0) {
            half = len >> 1;
            middle = from;
            middle = middle + half;
            if (value.compareTo(dates.get(middle)) == -1) {
                len = half;
            } else {
                from = middle;
                from++;
                len = len - half - 1;
            }
        }
        return from;
    }

    public static final Date maxDate() {return new Date(maximumSerialNumber());}

    public static final Date minDate() {return new Date(minimumSerialNumber());}


    /**
     * @Author  Jiaming Yan
     * @Description 返回当日SerialNumber
     */
    protected final long todaysSerialNumber() {
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        final int d = cal.get(java.util.Calendar.DAY_OF_MONTH);
        final int m = cal.get(java.util.Calendar.MONTH);
        final int y = cal.get(java.util.Calendar.YEAR);
        return fromDMY(d, m+1, y);
    }

    /**
     * @Author  Jiaming Yan
     * @Description SerialNumber赋值
     */
    protected final Date assign(final long serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

}
