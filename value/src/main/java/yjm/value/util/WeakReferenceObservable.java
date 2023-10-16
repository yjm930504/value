/*
 Copyright (C) 2007 Richard Gomes

 This source code is release under the BSD License.

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the JQuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.

 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package yjm.value.util;

import java.lang.ref.WeakReference;

/**
 * Observable的实现，将对观察者的引用保存为WeakReferences
 *
 */
public class WeakReferenceObservable extends DefaultObservable {

    public WeakReferenceObservable(final Observable observable) {
        super(observable);
    }

    @Override
    public void addObserver(final Observer referent) {
        super.addObserver(new WeakReferenceObserver(referent));
    }

    /**
     * This method deletes the Observer passed as argument but also discards those Observers which where reclaimed by gc
     */
    @Override
    public void deleteObserver(final Observer observer) {
        for (final Observer weakObserver : getObservers()) {
            final WeakReferenceObserver weakReference = (WeakReferenceObserver) weakObserver;
            final Observer referent = weakReference.get();
            if (referent == null || referent.equals(observer))
                deleteWeakReference(weakReference);
        }
    }

    private void deleteWeakReference(final WeakReferenceObserver observer){
        super.deleteObserver(observer);
    }


    //
    // inner classes
    //

    private class WeakReferenceObserver extends WeakReference<Observer> implements Observer {

        public WeakReferenceObserver(final Observer referent) {
            super(referent);
        }

        @Override
        //XXX::OBS public void update(final Observable o, final Object arg) {
        public void update() {
            final Observer referent = get();
            if (referent != null)
                //XXX::OBS referent.update(o, arg);
                referent.update();
            else
                deleteWeakReference(this);
        }
    }

}
