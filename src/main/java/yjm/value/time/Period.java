package yjm.value.time;

import yjm.value.lang.LibraryException;

import java.util.Formatter;
import java.util.Locale;

/**
 * @Author  Jiaming Yan
 * @Description Peirod类，length为Period的长度，units为Period的单位。
 * 如6M, Length = 6 ,units = Months
 */

public class Period implements Cloneable {

    private static final String UNKNOWN_FREQUENCY = "未知的频率";
    private static final String UNKNOWN_TIME_UNIT = "未知的时间单位";
    private static final String INCOMPATIBLE_TIME_UNIT = "不兼容的时间单位";
    private static final String UNDECIDABLE_COMPARISON = "无法确定的比较";
    private static final String DIVISION_BY_ZERO_ERROR = "不能除以0";






    //一年前
    public static final Period ONE_YEAR_FORWARD = new Period(1, TimeUnit.Years);
    //一年后
    public static final Period ONE_YEAR_BACKWARD = new Period(-1, TimeUnit.Years);
    //一个月前
    public static final Period ONE_MONTH_FORWARD = new Period(1, TimeUnit.Months);
    //一个月后
    public static final Period ONE_MONTH_BACKWARD = new Period(-1,TimeUnit.Months);
    //一天前
    public static final Period ONE_DAY_FORWARD = new Period(1, TimeUnit.Days);
    //一天后
    public static final Period ONE_DAY_BACKWARD = new Period(-1, TimeUnit.Days);

    private int length;

    private TimeUnit units;

    /**
     * @Author  Jiaming Yan
     * @Description 默认构造方法，Period的长度为0，时间单位为天数
     */
    public Period() {
        this.length = 0;
        this.units = TimeUnit.Days;
    }

    /**
     * @Author  Jiaming Yan
     * @param length Period长度
     * @param units 时间单位
     * @Description 设置Period长度和时间单位。
     */
    public Period(final int length, final TimeUnit units) {
        this.length = length;
        this.units = units;
    }

    /**
     * @Author  Jiaming Yan
     * @param f 频率
     * @Description 根据频率返回Period长度以及时间单位, 如：季度返回 Length = 3,时间单位 unit = month
     */
    public Period(final Frequency f) {
        switch (f) {
            case Once:
            case NoFrequency:
                units = TimeUnit.Days;
                length = 0;
                break;
            case Annual:
                units = TimeUnit.Years;
                length = 1;
                break;
            case Semiannual:
            case EveryFourthMonth:
            case Quarterly:
            case Bimonthly:
            case Monthly:
                units = TimeUnit.Months;
                length = 12 / f.toInteger();
                break;
            case EveryFourthWeek:
            case Biweekly:
            case Weekly:
                units = TimeUnit.Weeks;
                length = 52 / f.toInteger();
                break;
            case Daily:
                units = TimeUnit.Days;
                length = 1;
                break;
            default:
                throw new LibraryException(UNKNOWN_FREQUENCY);
        }
    }

    protected Period clone() {
        return new Period(this.length, this.units);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回时间长度为负的Period对象
     */
    public Period negative() {
        return new Period(-this.length(), this.units());
    }

    /**
     * @Author  Jiaming Yan
     * @Description Period相加，例如：1年 + 6个月，返回 18个月，不同时间单位只支持年与月相加和周与天相加
     */
    public Period addAssign(final Period another) {
        if (this.length() == 0) {
            this.length = another.length();
            this.units = another.units();
        } else if (this.units == another.units())
            this.length += another.length();
        else
            switch (this.units) {
                case Years:
                    switch (another.units()) {
                        case Months:
                            this.units = another.units();
                            //年度变为月度
                            this.length = this.length * 12 + another.length();
                            break;
                        case Weeks:
                        case Days:
                            throw new IllegalArgumentException(INCOMPATIBLE_TIME_UNIT);
                        default:
                            throw new LibraryException(UNKNOWN_TIME_UNIT);
                    }
                    break;
                case Months:
                    switch (another.units()) {
                        case Years:
                            this.length += another.length() * 12;
                            break;
                        case Weeks:
                        case Days:
                            throw new IllegalArgumentException(INCOMPATIBLE_TIME_UNIT);
                        default:
                            throw new LibraryException(UNKNOWN_TIME_UNIT);
                    }
                    break;
                case Weeks:
                    switch (another.units()) {
                        case Days:
                            this.units = another.units();
                            this.length = this.length * 7 + another.length();
                            break;
                        case Years:
                        case Months:
                            throw new IllegalArgumentException(INCOMPATIBLE_TIME_UNIT);
                        default:
                            throw new LibraryException(UNKNOWN_TIME_UNIT);

                    }
                    break;
                case Days:
                    switch (another.units()) {
                        case Weeks:
                            this.length += another.length() * 7;
                            break;
                        case Years:
                        case Months:
                            throw new IllegalArgumentException(INCOMPATIBLE_TIME_UNIT);
                        default:
                            throw new LibraryException(UNKNOWN_TIME_UNIT);
                    }
                    break;
                default:
                    throw new LibraryException(UNKNOWN_TIME_UNIT);
            }
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description Period相减，例如：1年 - 6个月，返回 6个月，不同时间单位只支持年与月和周与天相减
     */
    public Period subAssign(final Period another) {
        return this.addAssign(another.clone().negative());
    }

    /**
     * @Author  Jiaming Yan
     * @Description Period相除，例如：1年 / 2，返回 6个月，不同时间单位只支持年转换为月，周转换为天
     */
    public Period divAssign(final int scalar) {
        if (scalar == 0)
            throw new ArithmeticException(DIVISION_BY_ZERO_ERROR);

        if (this.length % scalar == 0)
            this.length /= scalar;
        else
            switch (this.units) {
                case Years:
                    this.units = TimeUnit.Months;
                    this.length *= 12;
                    break;
                case Weeks:
                    this.units = TimeUnit.Days;
                    this.length *= 7;
                    break;
            }

        if (this.length % scalar == 0)
            this.length = this.length/scalar;
        else
            throw new LibraryException("Period无法被" + scalar + "整除");
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回多期Period的对象，如：Period为1年，Period.mul(2)返回两年的Period对象
     */
    public Period mul(final int scalar) {
        return new Period(scalar * this.length, this.units);
    }

    /**
     * @Author  Jiaming Yan
     * @param another 用于相加的Period
     * @Description Period相加
     */
    public Period add(final Period another) {
        return this.clone().addAssign(another);
    }

    /**
     * @Author  Jiaming Yan
     * @param another 用于相减的Period
     * @Description Period相减
     */
    public Period sub(final Period another) {
        return this.clone().subAssign(another);
    }

    /**
     * @Author  Jiaming Yan
     * @param scalar 除数
     * @Description Period相除
     */
    public Period div(final int scalar) {
        return this.clone().divAssign(scalar);
    }

    /**
     * @Author  Jiaming Yan
     * @param another 用于比较Period
     * @Description 判断Period是否相等
     */
    public boolean eq(final Period another) {
        return this.equals(another);
    }
    /**
     * @Author  Jiaming Yan
     * @param another 用于比较时间
     * @Description 判断Period是否相等
     */
    public boolean neq(final Period another) {
        return !this.equals(another);
    }

    /**
     * @Author  Jiaming Yan
     * @param another 用于比较时间
     * @Description 判断Period是否大于另一个Period
     */
    public boolean gt(final Period another) {
        return another.lt(this);
    }

    /**
     * @Author  Jiaming Yan
     * @param another 用于比较时间
     * @Description 判断Period是否小于等于另一个Period
     */
    public boolean le(final Period another) {
        return this.lt(another) || this.eq(another);
    }

    /**
     * @Author  Jiaming Yan
     * @param another 用于比较时间
     * @Description 判断Period是否大于等于另一个Period
     */
    public boolean ge(final Period another) {
        return another.le(this);
    }

    /**
     * @Author  Jiaming Yan
     * @param another 用于比较时间
     * @Description Period是否小于另一个Period的判断逻辑
     */
    public boolean lt(final Period another) {
        if (this.length == 0)
            return (another.length > 0);
        if (another.length == 0)
            return (this.length < 0);

        //准确比较
        //先比较单位，再比较长度
        if (this.units() == another.units())
            return this.length() < another.length();
        if (this.units() == TimeUnit.Months && another.units() == TimeUnit.Years)
            return this.length() < 12 * another.length();
        if (this.units() == TimeUnit.Years && another.units() == TimeUnit.Months)
            return 12 * this.length() < another.length();
        if (this.units() == TimeUnit.Days && another.units() == TimeUnit.Weeks)
            return this.length() < 7 * another.length();
        if (this.units() == TimeUnit.Weeks && another.units() == TimeUnit.Days)
            return 7 * this.length() < another.length();

        //不准确比较
        final int period1MinDays = this.getMinDays();
        final int period1MaxDays = this.getMaxDays();
        final int period2MinDays = another.getMinDays();
        final int period2MaxDays = another.getMaxDays();

        if (period1MaxDays < period2MinDays)
            return true;
        else if (period1MinDays > period2MaxDays)
            return false;
        else
            throw new LibraryException(UNDECIDABLE_COMPARISON);
    }


    /**
     * @Author  Jiaming Yan
     * @Description 返回Period的时间长度
     */
    public final int length() {
        return this.length;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回Period的时间单位
     */
    public final TimeUnit units() {
        return this.units;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回Period对应的频率，如 Period = 3 Months, 返回Quarterly
     */
    public final Frequency frequency() {

        final int length = Math.abs(this.length);

        if (length == 0)
            return Frequency.NoFrequency;

        switch (units) {
            case Years:
                if (length == 1)
                    return Frequency.Annual;
                else
                    return Frequency.OtherFrequency;
            case Months:
                if (12 % length == 0 && length <= 12)
                    return Frequency.valueOf(12 / length);
                else
                    return Frequency.OtherFrequency;
            case Weeks:
                if (length == 1)
                    return Frequency.Weekly;
                else if (length == 2)
                    return Frequency.Biweekly;
                else if (length == 4)
                    return Frequency.EveryFourthWeek;
                else
                    return Frequency.OtherFrequency;
            case Days:
                if (length==1)
                    return Frequency.Daily;
                else
                    return Frequency.OtherFrequency;
            default:
                throw new LibraryException(UNDECIDABLE_COMPARISON);
        }
    }

    /**
     * @Author  Jiaming Yan
     * @Description 将Period转换为年，如 Period = 3 Months, 返回 3 / 12 = 0.25, 之用的时间单位是月度/年度的Period
     */
    public double years(final Period p) {

        if(p.length() == 0) return 0.0;

        switch (p.units()) {
            case Days:
            case Weeks:
                throw new IllegalArgumentException(UNDECIDABLE_COMPARISON);
            case Months:
                return p.length() / 12.0;
            case Years:
                return p.length();
            default:
                throw new LibraryException(UNKNOWN_TIME_UNIT);
        }
    }

    /**
     * @Author  Jiaming Yan
     * @Description 将Period转换为周，如 Period = 21 Days, 返回 21 / 7 = 3, 之用的时间单位是周/天的Period
     */
    public double weeks(final Period p) {
        if(p.length() == 0) return 0.0;

        switch(p.units()) {
            case Days:
                return p.length() / 7.0;
            case Weeks:
                return p.length();
            case Months:
            case Years:
                throw new IllegalArgumentException(UNDECIDABLE_COMPARISON);
            default:
                throw new LibraryException(UNKNOWN_TIME_UNIT);
        }
    }

    /**
     * @Author  Jiaming Yan
     * @Description 将Period转换为天，如 Period = 3 Weeks, 返回 3 * 7 = 21, 之用的时间单位是周/天的Period
     */
    public double days(final Period p) {

        if(p.length() == 0) return 0.0;

        switch(p.units()) {
            case Days:
                return p.length();
            case Weeks:
                return p.length() * 7.0;
            case Months:
            case Years:
                throw new IllegalArgumentException(UNDECIDABLE_COMPARISON);
            default:
                throw new LibraryException(UNKNOWN_TIME_UNIT);
        }
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + length;
        result = prime * result + ((units == null) ? 0 : units.hashCode());
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 判断Period对象是否相同
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        return obj instanceof Period &&
                ((Period) obj).fEquals(this);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 判断两个Period对象是否相同
     */
    protected boolean fEquals(Period other) {
        if (length != other.length)
            return false;
        if (units == null) {
            if (other.units != null)
                return false;
        } else if (!units.equals(other.units))
            return false;
        return true;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回Peirod对应的最小的天数，如 Period = 2 months ，返回 2 * 28 = 56天
     */
    private int getMinDays() {
        switch (this.units()) {
            case Years:
                return this.length()*365;
            case Months:
                return this.length()*28;
            case Weeks:
                return this.length()*7;
            case Days:
                return this.length();
            default:
                throw new LibraryException(UNKNOWN_TIME_UNIT);
        }
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回Peirod的对应的最大的天数，如 Period = 2 months ，返回 2 * 31 = 56天
     */
    private int getMaxDays() {
        switch (this.units()) {
            case Years:
                return this.length()*366;
            case Months:
                return this.length()*31;
            case Weeks:
                return this.length()*7;
            case Days:
                return this.length();
            default:
                throw new LibraryException(UNKNOWN_TIME_UNIT);
        }
    }


    @Override
    public String toString() {
        return getLongFormat();
    }

    public String getLongFormat() {
        return getInternalLongFormat();
    }

    public String getShortFormat() {
        return getInternalShortFormat();
    }


    /**
     * @Author  Jiaming Yan
     * @Description 返回Peirod格式，e.g. "2 weeks"
     */
    private String getInternalLongFormat() {
        String suffix;
        if (this.length == 1)
            suffix = "";
        else
            suffix = "s";
        final StringBuilder sb = new StringBuilder();
        final Formatter formatter = new Formatter(sb, Locale.US);
        formatter.format("%d %s %s", this.length, this.units.getLongFormat(),
                suffix);
        return sb.toString();
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回Peirod格式，e.g.  "2w"
     */
    private String getInternalShortFormat() {
        final StringBuilder sb = new StringBuilder();
        final Formatter formatter = new Formatter(sb, Locale.US);
        formatter.format("%d%s", this.length, this.units.getShortFormat());
        return sb.toString();
    }


    /**
     * @Author  Jiaming Yan
     * @Description 将以天Days为单位的Period转换为Weeks，以Months为单位的Period转换为Years
     */
    public void normalize() {
        if (length != 0)
            switch (units) {
                case Days:
                    if (!(length % 7 != 0)) {
                        length /= 7;
                        units = TimeUnit.Weeks;
                    }
                    break;
                case Months:
                    if (!(length % 12 != 0)) {
                        length /= 12;
                        units = TimeUnit.Years;
                    }
                    break;
                case Weeks:
                case Years:
                    break;
                default:
                    throw new LibraryException(UNKNOWN_TIME_UNIT);
            }
    }

}
