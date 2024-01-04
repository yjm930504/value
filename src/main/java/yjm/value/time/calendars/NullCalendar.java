package yjm.value.time.calendars;

import yjm.value.time.Calendar;
import yjm.value.time.Date;
import yjm.value.time.Weekday;

public class NullCalendar extends Calendar {

    public NullCalendar() {
        impl = new Impl();
    }

    private final class Impl extends Calendar.Impl {
        @Override
        public String name(){return "NULL";}

        @Override
        public boolean isWeekend(final Weekday weekday){
            return false;
        }
        @Override
        public boolean isBusinessDay(final Date date){return true;}
    }
}
