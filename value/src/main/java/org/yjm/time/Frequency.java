package org.yjm.time;

/**
 * @Author  Jiaming Yan
 * @Description 频率枚举类
 */

public enum Frequency {

    NoFrequency(-1),

    Once(0),

    Annual(1),

    Semiannual(2),

    EveryFourthMonth(3),

    Quarterly(4),

    Bimonthly(6),

    Monthly(12),

    EveryFourthWeek(13),

    Biweekly(26),

    Weekly(52),

    Daily(365),

    OtherFrequency(999);

    private final int enumValue;


    Frequency(final int frequency) {
        this.enumValue = frequency;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 根据value返回Frequency，如：365对应Daily
     */
    public static Frequency valueOf(final int value) {
        switch (value) {
            case -1:
                return Frequency.NoFrequency;
            case 0:
                return Frequency.Once;
            case 1:
                return Frequency.Annual;
            case 2:
                return Frequency.Semiannual;
            case 3:
                return Frequency.EveryFourthMonth;
            case 4:
                return Frequency.Quarterly;
            case 6:
                return Frequency.Bimonthly;
            case 12:
                return Frequency.Monthly;
            case 13:
                return Frequency.EveryFourthWeek;
            case 26:
                return Frequency.Biweekly;
            case 52:
                return Frequency.Weekly;
            case 365:
                return Frequency.Daily;
            case 999:
                return Frequency.OtherFrequency;
        }
        return null;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回频率对应的数值，例如：月度为12，季度为4，每日为365
     */
    public int toInteger() {
        return this.enumValue;
    }

}