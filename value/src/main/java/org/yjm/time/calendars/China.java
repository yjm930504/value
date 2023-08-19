package org.yjm.time.calendars;

import org.yjm.time.Calendar;
import org.yjm.time.Date;
import org.yjm.time.Month;
import org.yjm.time.Weekday;

import static org.yjm.time.Weekday.Saturday;
import static org.yjm.time.Weekday.Sunday;

public class China extends Calendar {

    public static enum Market {SSE}

    public China() {
        this(Market.SSE);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数，SseImpl对象
     */
    public China(final Market m) {
        switch (m) {
            case SSE:
                impl = new SseImpl();
                break;
            default:
                throw new RuntimeException(UNKNOWN_MARKET);
        }
    }

    private final class SseImpl extends Impl {

        @Override
        public String name() {
            return "上海证券交易所";
        }

        @Override
        public boolean isWeekend(final Weekday w) {
            return w == Saturday || w == Sunday;
        }

        @Override
        public boolean isBusinessDay(final Date date) {
            // 目前只考虑周末
            final Weekday w = date.weekday();
            final int d = date.dayOfMonth();
            final Month m = date.month();
            final int y = date.year();

            if (isWeekend(w)){
                return false;
            }
            return true;
        }
    }


}
