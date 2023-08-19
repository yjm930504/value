package org.yjm.time.calendars;

import org.yjm.time.Calendar;
import org.yjm.time.Date;
import org.yjm.time.Weekday;

public class NullCalendar extends Calendar {

    public NullCalendar() {
        impl = new Impl();
    }

    private final class Impl extends Calendar.Impl {
        @Override
        public String name(){return "Null";}

        @Override
        public boolean isWeekend(final Weekday weekday){
            return false;
        }
        @Override
        public boolean isBusinessDay(final Date date){return true;}
    }
}
