package yjm.value.util;


import java.util.List;

/**
 * @Author  Jiaming Yan
 * @Description 计算和结果缓存框架
 */
public abstract class LazyObject implements Observer, Observable{


    /**
     * @Author  Jiaming Yan
     * @Description 是否已计算
     */
    protected boolean calculated;
    protected boolean frozen;

    protected abstract void performCalculations() throws ArithmeticException;

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    public LazyObject() {
        this.calculated = false;
        this.frozen = false;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 强制重新计算
     */
    public final void recalculate() {
        final boolean wasFrozen = frozen;
        calculated = frozen = false;
        try {
            calculate();
        } finally {
            frozen = wasFrozen;
            notifyObservers();
        }
    }
    /**
     * @Author  Jiaming Yan
     * @Description 用于约束对象在连续调用时返回当前缓存的结果
     */
    public final void freeze() {
        frozen = true;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 恢复冻结，从而重新计算
     */
    public final void unfreeze() {
        frozen = false;
        notifyObservers();
    }

    /**
     * @Author  Jiaming Yan
     * @Description 调用performCalculations方法来执行所有需要的计算
     */
    protected void calculate() {
        if (!calculated && !frozen) {
            calculated = true;
            try {
                performCalculations();
            } catch (final ArithmeticException e) {
                calculated = false;
                throw e;
            }
        }
    }


    // implements Observer

    /**
     * @Author  Jiaming Yan
     * @Description
     */
    public void update() {
        if (!frozen && calculated)
            notifyObservers();
        calculated = false;
    }

    // implements Observable

    private final Observable delegatedObservable = new DefaultObservable(this);
    @Override
    public final void addObserver(final Observer observer) {
        delegatedObservable.addObserver(observer);
    }
    @Override
    public final int countObservers() {
        return delegatedObservable.countObservers();
    }
    @Override
    public final void deleteObserver(final Observer observer) {
        delegatedObservable.deleteObserver(observer);
    }
    @Override
    public final void notifyObservers() {
        delegatedObservable.notifyObservers();
    }
    @Override
    public final void notifyObservers(final Object arg) {
        delegatedObservable.notifyObservers(arg);
    }
    @Override
    public final void deleteObservers() {
        delegatedObservable.deleteObservers();
    }
    @Override
    public final List<Observer> getObservers() {
        return delegatedObservable.getObservers();
    }


}
