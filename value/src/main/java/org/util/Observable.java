package org.util;

import java.util.List;

public interface Observable {


    public void addObserver(final Observer observer);

    public int countObservers();

    public List<Observer> getObservers();

    public void deleteObserver(final Observer observer);

    public void deleteObservers();

    public void notifyObservers();

    public void notifyObservers(Object arg);



}
