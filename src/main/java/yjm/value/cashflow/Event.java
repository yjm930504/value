package yjm.value.cashflow;


import yjm.value.lang.exceptions.LibraryException;
import yjm.value.time.Date;
import yjm.value.util.*;
import yjm.value.Settings;

import java.util.List;

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

    private final DefaultObservable delegatedObservable = new DefaultObservable(this);

    @Override
    public void addObserver(final Observer observer) {
        delegatedObservable.addObserver(observer);
    }

    @Override
    public int countObservers() {
        return delegatedObservable.countObservers();
    }

    @Override
    public void deleteObserver(final Observer observer) {
        delegatedObservable.deleteObserver(observer);
    }

    @Override
    public void notifyObservers() {
        delegatedObservable.notifyObservers();
    }

    @Override
    public void notifyObservers(final Object arg) {
        delegatedObservable.notifyObservers(arg);
    }

    @Override
    public void deleteObservers() {
        delegatedObservable.deleteObservers();
    }

    @Override
    public List<Observer> getObservers() {
        return delegatedObservable.getObservers();
    }

    @Override
    public void accept(final PolymorphicVisitor pv) {
        final Visitor<Event> v = (pv!=null) ? pv.visitor(this.getClass()) : null;
        if (v != null) {
            v.visit(this);
        } else {
            throw new LibraryException("null event visitor");
        }
    }



}
