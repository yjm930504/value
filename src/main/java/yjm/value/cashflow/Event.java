package yjm.value.cashflow;


import yjm.value.time.Date;
import yjm.value.util.DefaultObservable;
import yjm.value.util.Observable;
import yjm.value.util.PolymorphicVisitable;
import yjm.value.Settings;

/**
 * @Author  Jiaming Yan
 * @Description 金融事件类
 */
public abstract class Event implements Observable, PolymorphicVisitable {

    protected Event(){}

    public abstract Date date();

    /**
     * @Author  Jiaming Yan
     * @Description 判断是否已发生
     */
    public boolean hasOccurred(final Date d)  {
        return hasOccurred(d, new Settings().isTodaysPayments());
    }

    /**
     * @Author  Jiaming Yan
     * @Description 判断是否已发生
     */
    public boolean hasOccurred(final Date d, final boolean includeToday){
        if (includeToday) {
            return date().compareTo(d) < 0;
        } else {
            return date().compareTo(d) <= 0;
        }
    }

    // implements Observable
    private final DefaultObservable delegatedObservable = new DefaultObservable(this);



}
