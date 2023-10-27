package yjm.value.quotes;

import yjm.value.util.Observable;


public class RelinkableHandle<T extends Observable> extends Handle<T> {

    public RelinkableHandle() {
        super();
    }

	public RelinkableHandle(final T observable) {
    	this(observable, true);
    }

    public RelinkableHandle(final T observable, final boolean isObserver) {
    	super(observable, isObserver);
    }

    @Override
    public final void linkTo(final T observable) {
    	super.internalLinkTo(observable, true);
    }

    @Override
    public final void linkTo(final T observable, final boolean isObserver) {
    	super.internalLinkTo(observable, isObserver);
    }

}
