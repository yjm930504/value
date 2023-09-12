package yjm.value.util;

import yjm.value.QL;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultObservable implements Observable{

    final private static String OBSERVABLE_IS_NULL = "观察者为空";
    final private static String CANNOT_NOTIFY_OBSERVERS = "could not notify one or more observers";

    private final List<Observer> observers;
    private final Observable observable;

    public DefaultObservable(final Observable observable) {
        QL.require(observable != null, DefaultObservable.OBSERVABLE_IS_NULL);
        this.observers = new CopyOnWriteArrayList<Observer>();
        this.observable = observable;
    }

    @Override
    public void addObserver(Observer observer) {

    }

    @Override
    public int countObservers() {
        return 0;
    }

    @Override
    public List<Observer> getObservers() {
        return null;
    }

    @Override
    public void deleteObserver(Observer observer) {

    }

    @Override
    public void deleteObservers() {

    }

    @Override
    public void notifyObservers() {

    }

    @Override
    public void notifyObservers(Object arg) {

    }
}
