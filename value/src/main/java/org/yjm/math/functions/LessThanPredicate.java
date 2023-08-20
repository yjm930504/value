package org.yjm.math.functions;

import org.yjm.math.Ops;

public final class LessThanPredicate implements Ops.BinaryDoublePredicate{

    @Override
    public boolean op(final double a, final double b) {
        return a < b;
    }

}
