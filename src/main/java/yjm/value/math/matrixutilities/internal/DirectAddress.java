package yjm.value.math.matrixutilities.internal;

import yjm.value.lang.exceptions.LibraryException;

import java.util.EnumSet;
import java.util.Set;



/**
 * @Author  Jiaming Yan
 * @Description DirectAddress抽象类
 */
public abstract class DirectAddress implements Address, Cloneable{

    /**
     * @Author  Jiaming Yan
     * @Description 存储数据
     */
    protected final double[] data;

    /**
     * @Author  Jiaming Yan
     * @Description
     */
    protected final int row0;

    /**
     * @Author  Jiaming Yan
     * @Description
     */
    protected final int row1;

    /**
     * @Author  Jiaming Yan
     * @Description
     */
    protected final Address chain;

    /**
     * @Author  Jiaming Yan
     * @Description
     */
    protected final int col0;

    /**
     * @Author  Jiaming Yan
     * @Description
     */
    protected final int col1;

    /**
     * @Author  Jiaming Yan
     * @Description flags
     */
    protected final Set<Address.Flags> flags;

    /**
     * @Author  Jiaming Yan
     * @Description 是否C连续
     */
    protected final boolean contiguous;

    /**
     * @Author  Jiaming Yan
     * @Description 行数
     */
    protected final int rows;

    /**
     * @Author  Jiaming Yan
     * @Description 列数
     */
    protected final int cols;

    /**
     * @Author  Jiaming Yan
     * @Description 偏移,Fortan连续为1，否则为0
     */
    protected final int offset;

    /**
     * @Author  Jiaming Yan
     * @Description
     */
    private final int base;

    /**
     * @Author  Jiaming Yan
     * @Description
     */
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
            final int col0,
            final int col1,
            final Set<Address.Flags> flags,
            final boolean contiguous,
            final int rows,
            final int cols) {
        this.data = data;
        this.chain  = chain;
        this.contiguous = contiguous;
        this.flags  = (flags != null) ? flags : (chain != null) ? chain.flags() : EnumSet.noneOf(Address.Flags.class);
        this.offset = isFortran() ? 1 : 0;
        this.row0 = row0 - offset + ( chain == null ? 0 : chain.row0() );
        this.col0 = col0 - offset + ( chain == null ? 0 : chain.col0() );
        this.row1 = this.row0 + (row1 - row0);
        this.col1 = this.col0 + (col1 - col0);
        this.rows = (chain==null) ? rows : chain.rows();
        this.cols = (chain==null) ? cols : chain.cols();
        this.base = (row0 - offset) * cols + (col0 - offset);
        this.last = (row1 - offset -1) * cols + (col1 - offset - 1);
    }


    /**
     * @Author  Jiaming Yan
     * @Description 判断C连续
     */
    @Override
    public boolean isContiguous() {
        return contiguous;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 判断Fortan连续
     */
    @Override
    public boolean isFortran() {
        return flags.contains(Address.Flags.FORTRAN);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回flags
     */
    @Override
    public Set<Address.Flags> flags() {
        return flags;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回行数
     */
    @Override
    public int rows() {
        return rows;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回列数
     */
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

    /**
     * @Author  Jiaming Yan
     * @Description DirectAddressOffset抽象类
     */
    protected abstract class DirectAddressOffset implements Address.Offset {
        protected int row;
        protected int col;
        protected DirectAddressOffset() {}

    }

}
