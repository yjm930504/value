package org.yjm.math.matrixutilities.internal;

import java.util.EnumSet;
import java.util.Set;

public abstract class DirectAddress {
    protected final double[] data;
    protected final int row0;
    protected final int row1;
    protected final Address chain;
    protected final int col0;
    protected final int col1;
    protected final Set<Address.Flags> flags;
    protected final boolean contiguous;
    protected final int rows;
    protected final int cols;

    protected final int offset;

    private final int base;
    private final int last;

    public DirectAddress(
            final double[] data,
            final int row0, final int row1,
            final Address chain,
            final int col0, final int col1,
            final Set<Address.Flags> flags,
            final boolean contiguous,
            final int rows, final int cols) {
        this.data = data; // DO NOT use clone: direct reference on purpose!
        this.chain  = chain;
        this.contiguous = contiguous;
        this.flags  = (flags != null) ? flags : (chain != null) ? chain.flags() : EnumSet.noneOf(Address.Flags.class);

        this.offset = isFortran() ? 1 : 0;
        this.row0 = row0 - offset + ( chain==null ? 0 : chain.row0() );
        this.col0 = col0 - offset + ( chain==null ? 0 : chain.col0() );
        this.row1 = this.row0 + (row1-row0);
        this.col1 = this.col0 + (col1-col0);
        this.rows = (chain==null) ? rows : chain.rows();
        this.cols = (chain==null) ? cols : chain.cols();
        this.base = (row0-offset)*cols + (col0-offset);
        this.last = (row1-offset-1)*cols + (col1-offset-1);
    }
}
