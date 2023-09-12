package yjm.value.time;

/**
 * @Author  Jiaming Yan
 * @Description 定义时间单位，Days, Weeks, Months, Years
 */

public enum TimeUnit {

    Days, Weeks, Months, Years;

    /**
     * @Author  Jiaming Yan
     * @Description 小写并去掉单词尾部的s，如'Weeks'变更为'week'
     */
    public String getLongFormat() {
        return getLongFormatString();
    }

    /**
     * @Author  Jiaming Yan
     * @Description 取units的第一位，如'Weeks'返回'W'
     */
    public String getShortFormat() {
        return getShortFormatString();
    }

    /**
     * @Author  Jiaming Yan
     * @Description 小写并去掉单词尾部的s，如'Weeks'变更为'week'
     */
    private String getLongFormatString() {
        StringBuilder sb = new StringBuilder();
        sb.append(toString().toLowerCase());
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    /**
     * @Author  Jiaming Yan
     * @Description 取units的第一位，如'Weeks'返回'W'
     */
    private String getShortFormatString() {
        return String.valueOf(toString().charAt(0));
    }
}
