package yjm.value.math.matrixutilities.internal;

import java.util.ListIterator;
import java.util.Set;



/**
 * @Author  Jiaming Yan
 * @Description Address接口
 */
public interface Address {

    public static final String INVALID_BACKWARD_INDEXING = "invalid backward indexing";
    public static final String INVALID_ROW_INDEX = "无效行索引";
    public static final String INVALID_COLUMN_INDEX = "无效列索引";
    public static final String GAP_INDEX_FOUND = "gap index found";

    /**
     * @Author  Jiaming Yan
     * @Description 行数
     */
    public int rows();
    /**
     * @Author  Jiaming Yan
     * @Description 列数
     */
    public int cols();
    /**
     * @Author  Jiaming Yan
     * @Description
     */
    public int base();

    /**
     * @Author  Jiaming Yan
     * @Description
     */
    public int last();
    /**
     * @Author  Jiaming Yan
     * @Description 矩阵行的偏移量
     */
    public int row0();
    /**
     * @Author  Jiaming Yan
     * @Description 矩阵列的偏移量
     */
    public int col0();

    /**
     * @Author  Jiaming Yan
     * @Description 判断C连续
     */
    public boolean isContiguous();

    /**
     * @Author  Jiaming Yan
     * @Description 判断Fortan连续
     */
    public boolean isFortran();

    /**
     * @Author  Jiaming Yan
     * @Description Flag
     */
    public Set<Address.Flags> flags();

    public enum Flags {FORTRAN,}
    /**
     * @Author  Jiaming Yan
     * @Description
     */
    public interface Offset {
        public abstract int op();
    }
    /**
     * @Author  Jiaming Yan
     * @Description Array接口
     */
    public interface ArrayAddress extends Cloneable, Address {

        public ArrayAddress clone();
        public int op(int index);
        public ArrayAddress toFortran();
        public ArrayAddress toJava();
        public ArrayOffset offset();
        public ArrayOffset offset(int index);
        public interface ArrayOffset extends Offset, ListIterator<Double> {
            public void setIndex(final int index);
        }
    }
    /**
     * @Author  Jiaming Yan
     * @Description Matrix接口
     */
    public interface MatrixAddress extends Cloneable, Address {

        /**
         * @Author  Jiaming Yan
         * @Description 复制Matrix对象
         */
        public MatrixAddress clone();

        /**
         * @Author  Jiaming Yan
         * @Description
         */
        public int op(int row, int col);
        public MatrixAddress toFortran();
        public MatrixAddress toJava();
        public MatrixOffset offset();
        public MatrixOffset offset(final int row, final int col);
        public interface MatrixOffset extends Offset {
            public void setRow(final int row);
            public void setCol(final int col);
            public void nextRow();
            public void prevRow();
            public void nextCol();
            public void prevCol();
        }

    }

}
