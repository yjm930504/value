package org.yjm.time;

import org.yjm.lang.LibraryException;

/**
 * @author Jiaming Yan
 * @description: 计息区间调整规则类
 */
public class DateGeneration {


    public static enum Rule{

        /**
         * 以起息日作为最后一个计息区间的结束日期，以计息频率为间隔（如3M），确定最后一个计息区间开始日期，并以此作为下一计息区间的结束日期。
         */
        Backward   (1),

        /**
         * 以起息日作为第一个计息区间开始日期，以计息频率为间隔（如3M），确定第一个计息区间结束日期，并以此作为下一计息区间的起始日期。
         */
        Forward   (2),

        /**
         * 只产生一个计息区间，以起息日作为计息区间开始日期，以到期日作为计息区间的结束日期。
         */
        Zero   (3),

        /**
         * 以起息日作为第一个计息区间开始日期，以计息频率为间隔（如3M），确定第一个计息区间结束日期所在月份，以此月第三个周三作为第一个计息区间结束日期，并以此作为第二个计息区间的开始日期。
         */
        ThirdWednesday   (4);

        private final int rule;

        private static final String UNKNOWN_DATE_GENERATION_RULE = "未知的计息区间调整规则";

        private Rule(final int rule) {
            this.rule = rule;
        }

        @Override
        public String toString() {
            switch (rule) {
                case 1:
                    return "Backward";
                case 2:
                    return "Forward";
                case 3:
                    return "Zero";
                case 4:
                    return "ThirdWednesday";
                default:
                    throw new LibraryException(UNKNOWN_DATE_GENERATION_RULE);
            }
        }

    }
}
