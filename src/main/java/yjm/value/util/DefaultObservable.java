package yjm.value.util;

import yjm.value.QL;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultObservable implements Observable{

    final private static String OBSERVABLE_IS_NULL = "观察者为空";
    final private static String CANNOT_NOTIFY_OBSERVERS = "无法通知观察者";

    private final List<Observer> observers;
    private final Observable observable;

    public DefaultObservable(final Observable observable) {
        QL.require(observable != null, DefaultObservable.OBSERVABLE_IS_NULL);
        this.observers = new CopyOnWriteArrayList<Observer>();
        this.observable = observable;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public int countObservers() {
        return observers.size();
    }

    @Override
    public List<Observer> getObservers() {
        return Collections.unmodifiableList(this.observers);
    }

    @Override
    public void deleteObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void deleteObservers() {
        observers.clear();
    }

    @Override
    public void notifyObservers() {
        notifyObservers(null);
    }

    @Override
    public void notifyObservers(Object arg) {
        Exception exception = null;
        for (final Observer observer : observers) {
            try {
                wrappedNotify(observer, observable, arg);
            } catch (final Exception e) {
                exception = e;
            }
        }
        if (exception!=null) QL.error(DefaultObservable.CANNOT_NOTIFY_OBSERVERS, exception);
    }

    protected void wrappedNotify(final Observer observer, final Observable observable, final Object arg) {
        observer.update();
    }
}
