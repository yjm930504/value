package yjm.value.util;

import yjm.value.Settings;

import java.util.List;


/**
 * 观察者模式
 * <p>
 * QuantLib 的设计初衷是提供一个生产环境下的实时计算框架。
 * <p>
 * 生产环境中的计算任务通常需要某种机制可以触发自动计算，而观察者模式用来实现这一功能。
 * <p>
 * 正是实时计算的要求，要保证所有的计算都在相同的估值日期发生，这也就是为什么作为单体模式实现的 {@link Settings} 中存在 {@link Settings#evaluationDate}方法。
 *
 */
public interface Observable {


    public void addObserver(final Observer observer);

    public int countObservers();

    public List<Observer> getObservers();

    public void deleteObserver(final Observer observer);

    public void deleteObservers();

    public void notifyObservers();

    public void notifyObservers(Object arg);



}
