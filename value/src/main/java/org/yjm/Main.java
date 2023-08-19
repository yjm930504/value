package org.yjm;


import org.yjm.time.*;
import org.yjm.time.calendars.china;


public class Main {


    public static void main(String[] args) {

        int wd = 0;

        Date from = new Date(12,6,2023);
        Date to = new Date(17,6,2023);
        Date mid = new Date(17,6,2023);

        Schedule s = new Schedule(from, to ,new Period(2, TimeUnit.Days),new china(), BusinessDayConvention.Following,BusinessDayConvention.Following, DateGeneration.Rule.Forward,false);
//        System.out.println(wd);
        Date d1 = s.previousDate(mid);
        int i1 = s.size();
        int i = Date.lowerBound(s.dates(),mid);
        System.out.println(i);
    }
}
