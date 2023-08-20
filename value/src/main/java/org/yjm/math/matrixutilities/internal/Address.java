package org.yjm.math.matrixutilities.internal;

import java.util.ListIterator;
import java.util.Set;

public interface Address {

    public static final String INVALID_BACKWARD_INDEXING = "invalid backward indexing";
    public static final String INVALID_ROW_INDEX = "invalid row index";
    public static final String INVALID_COLUMN_INDEX = "invalid column index";
    public static final String GAP_INDEX_FOUND = "gap index found";
    public int rows();
    public int cols();
    public int base();
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
    public boolean isContiguous();
    public boolean isFortran();
    public Set<Address.Flags> flags();
    public enum Flags {FORTRAN,}
    public interface Offset {
        public abstract int op();
    }

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

    public interface MatrixAddress extends Cloneable, Address {

        public MatrixAddress clone();
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