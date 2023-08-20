package org.yjm.math.matrixutilities.internal;

import java.util.Set;

public class DirectArrayRowAddress extends DirectAddress implements Address.ArrayAddress{

    public DirectArrayRowAddress(
            final double[] data,
            final int row,
            final Address chain,
            final int col0, final int col1,
            final Set<Flags> flags,
            final boolean contiguous,
            final int rows, final int cols) {
        super(data, row, row+1, chain, col0, col1, flags, contiguous, rows, cols);
    }



}
