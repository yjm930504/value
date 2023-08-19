package org.yjm.time;


/**
 * @Author  Jiaming Yan
 * @Description Weekday枚举类，Sunday(1), Monday(2), Tuesday(3), Wednesday(4), Thursday(5), Friday(6), Saturday(7)
 * Valueof() 根据数字转换为星期，value()返回星期数字
 */
public enum Weekday {

    Sunday(1), Monday(2), Tuesday(3), Wednesday(4), Thursday(5), Friday(6), Saturday(7);

    private final int enumValue;

    private Weekday(final int weekday) {
        this.enumValue = weekday;
    }


    public static Weekday valueOf(final int value) {
        switch (value) {
            case 1:
                return Weekday.Sunday;
            case 2:
                return Weekday.Monday;
            case 3:
                return Weekday.Tuesday;
            case 4:
                return Weekday.Wednesday;
            case 5:
                return Weekday.Thursday;
            case 6:
                return Weekday.Friday;
            case 7:
                return Weekday.Saturday;
        }
        return null;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回Weekday对应的数字
     */
    public int value() {
        return enumValue;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 数字转换为Weekday字符
     */
    @Override
    public String toString() {
        switch (enumValue) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            default:
                throw new RuntimeException("数字应该在1~7之间");
        }
    }

    /**
     * @Author  Jiaming Yan
     * @Description 数字转换为Weekday字符
     */
    public String getLongFormat() {
        return this.toString();
    }

    /**
     * @Author  Jiaming Yan
     * @Description 取Weekday前三个字符
     */
    public String getShortFormat() {
        return getAsShortFormat();
    }

    public String getShortestFormat() {
                return getAsShortestFormat();
    }

    private String getAsShortFormat (){
        final StringBuilder sb = new StringBuilder();
        sb.append(this);
        sb.setLength(3);
        return sb.toString();
    }

    /**
     * @Author  Jiaming Yan
     * @Description 取Weekday前两个字符
     */
    private String getAsShortestFormat(){
        final StringBuilder sb = new StringBuilder();
        sb.append(this);
        sb.setLength(2);
        return sb.toString();
    }

}
