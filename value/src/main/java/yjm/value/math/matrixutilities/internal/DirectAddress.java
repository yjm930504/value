package yjm.value.math.matrixutilities.internal;

import yjm.value.lang.LibraryException;

import java.util.EnumSet;
import java.util.Set;



/**
 * @Author  Jiaming Yan
 * @Description DirectAddress抽象类
 */
public abstract class DirectAddress implements Address, Cloneable{

    /**
     * @Author  Jiaming Yan
     * @Description
     */
    protected final double[] data;

    protected final int row0;
    protected final int row1;
    protected final Address chain;
    protected final int col0;

    protected final int col1;
    protected final Set<Address.Flags> flags;
    protected final boolean contiguous;
    //行数
    protected final int rows;
    //列数
    protected final int cols;
    //偏移
    protected final int offset;
    private final int base;
    private final int last;


    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    public DirectAddress(
            final double[] data,
            final int row0,
            final int row1,
            final Address chain,
            final int col0, final int col1,
            final Set<Address.Flags> flags,
            final boolean contiguous,
            final int rows, final int cols) {
        this.data = data;
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

    @Override
    public boolean isContiguous() {
        return contiguous;
    }

    @Override
    public boolean isFortran() {
        return flags.contains(Address.Flags.FORTRAN);
    }

    @Override
    public Set<Address.Flags> flags() {
        return flags;
    }

    @Override
    public int rows() {
        return rows;
    }

    @Override
    public int cols() {
        return cols;
    }

    @Override
    public int row0() {
        return row0;
    }

    @Override
    public int col0() {
        return col0;
    }

    @Override
    public int base() {
        return base;
    }

    @Override
    public int last() {
        return last;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("[row0=").append(row0).append(" row1=").append(row1);
        sb.append(" col0=").append(col0).append(" col1=").append(col1);
        sb.append(" flags=").append(flags).append("]");
        return sb.toString();
    }

    /**
     * @Author  Jiaming Yan
     * @Description 复制DirectAddress对象
     */
    @Override
    public DirectAddress clone() {
        try {
            return (DirectAddress) super.clone();
        } catch (final Exception e) {
            throw new LibraryException(e);
        }
    }

    protected abstract class DirectAddressOffset implements Address.Offset {
        protected int row;
        protected int col;
        protected DirectAddressOffset() {}

    }

}
