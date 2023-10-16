package yjm.value.quotes;

import yjm.value.util.Observable;
import yjm.value.util.Observer;
import yjm.value.util.WeakReferenceObservable;
import java.util.List;

/**
 * 借助内部类 {@link Link} 同时扮演观察者和被观察对象的角色，也就是充当“传令官”
 */
public class Handle<T extends Observable> implements Observable {

    private static final String EMPTY_HANDLE = "empty Handle cannot be dereferenced";

    /**
     * 负责将来自Observable对象的通知转发Observer对象
     */
    private final Link link;

    private T observable;
    private boolean isObserver = false;

    public Handle() {
        this.link = new Link(this);
        this.observable = null; // just for verbosity
        this.isObserver = true;
    }

    public Handle(final T observable) {
        this.link = new Link(this);
        internalLinkTo(observable, true);
    }


    public Handle(final T observable, final boolean registerAsObserver) {
        this.link = new Link(this);
        internalLinkTo(observable, registerAsObserver);
    }


    //
    // final public methods
    //

    final public boolean empty() /* @ReadOnly */ {
        return (this.observable==null);
    }

    final public T currentLink() {
        return this.observable;
    }


    //
    // public methods
    //

    public void linkTo(final T observable) {
        throw new UnsupportedOperationException();
    }

    public void linkTo(final T observable, final boolean registerAsObserver) {
        throw new UnsupportedOperationException();
    }


    //
    // protected final methods
    //

    final protected void internalLinkTo(final T observable) {
        this.internalLinkTo(observable, true);
    }

    final protected void internalLinkTo(final T observable, final boolean registerAsObserver) {
        if ((this.observable!=observable) || (this.isObserver!=registerAsObserver)) {
            if (this.observable!=null && this.isObserver) {
                this.observable.deleteObserver(link);
            }
            this.observable = observable;
            this.isObserver = registerAsObserver;
            if (this.observable!=null && this.isObserver) {
                this.observable.addObserver(link);
            }
            if (this.observable!=null) {
                this.observable.notifyObservers();
            }
        }
    }


    //
    // overrides Object
    //

    @Override
    public String toString() {
        return observable==null ? "null" : observable.toString();
    }


    //
    // implements Observable
    //

    @Override
    public final void addObserver(final Observer observer) {
        //XXX QL.require(observable!=null, EMPTY_HANDLE);
        link.addObserver(observer);
    }

    @Override
    public final int countObservers() {
        //XXX QL.require(observable!=null, EMPTY_HANDLE);
        return link.countObservers();
    }

    @Override
    public final void deleteObserver(final Observer observer) {
        //XXX QL.require(observable!=null, EMPTY_HANDLE);
        link.deleteObserver(observer);
    }

    @Override
    public final void notifyObservers() {
        //XXX QL.require(observable!=null, EMPTY_HANDLE);
        link.notifyObservers();
    }

    @Override
    public final void notifyObservers(final Object arg) {
        //XXX QL.require(observable!=null, EMPTY_HANDLE);
        link.notifyObservers(arg);
    }

    @Override
    public final void deleteObservers() {
        //XXX QL.require(observable!=null, EMPTY_HANDLE);
        link.deleteObservers();
    }

    @Override
    public final List<Observer> getObservers() {
        //XXX QL.require(observable!=null, EMPTY_HANDLE);
        return link.getObservers();
    }


    //
    // private final inner classes
    //

    /**
     * A Link is responsible for observing the Observable object passed to Handle during it's construction
     * or another Observable passed to {@link Handle#linkTo(Observable)} methods.
     * <p>
     * So, the ditto Observable notifies its Observers, a Link instance is notified, which ultimately
     * is responsible for forwarding this notification to a list of external Observers.
     */
    final private class Link extends WeakReferenceObservable implements Observer {

        private Link(final Observable observable) {
            super(observable);
        }

        @Override
        public void update() {
            if (observable!=null) {
                super.notifyObservers();
            }
        }
    }

}
