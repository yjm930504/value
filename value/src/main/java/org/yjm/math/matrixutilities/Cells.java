package org.yjm.math.matrixutilities;


import org.yjm.math.matrixutilities.internal.Address;
import org.yjm.lang.LibraryException;
import java.util.Set;

/**
 * @Author  Jiaming Yan
 * @Description Cells类，方便后续计算处理
 */
public abstract class Cells<T extends Address> implements Cloneable  {
    private final static String FORTRAN_ADDRESSING_EXPECTED = "variable \"%s\" should be FORTRAN-style addressing";
    protected final static String INVALID_ARGUMENTS = "无效参数";
    protected final static String WRONG_BUFFER_LENGTH = "缓冲区长度错误";
    protected final static String MATRIX_IS_INCOMPATIBLE = "不兼容的矩阵";
    protected final static String ARRAY_IS_INCOMPATIBLE = "不兼容的数组";
    protected final static String ITERATOR_IS_INCOMPATIBLE = "iterator is incompatible";
    protected final static String NOT_ENOUGH_STORAGE = "not enough storage area for operation";
    protected final static String MATRIX_MUST_BE_SQUARE = "矩阵需要是方矩阵";
    protected final static String MATRIX_MUST_BE_SYMMETRIC = "matrix must be symmetric";
    protected final static String MATRIX_IS_SINGULAR = "matrix is singular";
    protected final static String NON_CONTIGUOUS_DATA = "不支持非连续数据";

    protected final int rows;
    protected final int cols;
    protected final int size;

    protected T addr;
    /**
     * @Author  Jiaming Yan
     * @Description 保存数据
     */
    public double[] $;

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    protected Cells(
            final int rows,
            final int cols,
            final T addr) {
        this.rows = rows;
        this.cols = cols;
        this.addr = addr;
        this.size = rows * cols;
        this.$ = new double[size];
    }

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    protected Cells(
            final int rows,
            final int cols,
            final double data[],
            final T addr) {
        this.rows = rows;
        this.cols = cols;
        this.$ = data;
        this.addr = addr;
        this.size = rows * cols;
        if (data.length != addr.rows() * addr.cols())
            throw new IllegalArgumentException("维度与存储大小不匹配");
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回行数
     */
    public final int rows()       { return rows; }
    /**
     * @Author  Jiaming Yan
     * @Description 返回列数
     */
    public final int columns()    { return cols; }
    /**
     * @Author  Jiaming Yan
     * @Description 返回列数
     */
    public final int cols()       { return cols; }
    /**
     * @Author  Jiaming Yan
     * @Description 返回SIZE
     */
    public final int size()       { return size; }
    /**
     * @Author  Jiaming Yan
     * @Description 判断SIZE是否小于0
     */
    public final boolean empty() { return size <= 0; }
    /**
     * @Author  Jiaming Yan
     * @Description 返回flags
     */
    public final Set<Address.Flags> flags() {
        return addr.flags();
    }

    /**
     * @Author  Jiaming Yan
     * @Description 复制CELL对象
     */
    public Cells clone() {
        try {
            return (Cells) super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new LibraryException(e);
        }
    }

}

