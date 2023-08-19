package org.yjm.time;

/**
 * @Author  Jiaming Yan
 * @Description 月度枚举类，value()返回对应月度数字，valueof()根据数字返回月度
 */
public enum Month {
    January   (1),
    February  (2),
    March     (3),
    April     (4),
    May       (5),
    June      (6),
    July      (7),
    August    (8),
    September (9),
    October   (10),
    November  (11),
    December  (12);

    private final int enumValue;

    private Month(final int month) {
        this.enumValue = month;
    }

    public int value() {
        return enumValue;
    }

    public static Month valueOf(final int month) {
        final Month returnMonth;
        switch (month) {
            case 1 -> returnMonth = Month.January;
            case 2 -> returnMonth = Month.February;
            case 3 -> returnMonth = Month.March;
            case 4 -> returnMonth = Month.April;
            case 5 -> returnMonth = Month.May;
            case 6 -> returnMonth = Month.June;
            case 7 -> returnMonth = Month.July;
            case 8 -> returnMonth = Month.August;
            case 9 -> returnMonth = Month.September;
            case 10 -> returnMonth = Month.October;
            case 11 -> returnMonth = Month.November;
            case 12 -> returnMonth = Month.December;
            default -> throw new RuntimeException("月度数字应在1~12之间");
        }
        return returnMonth;
    }

}
