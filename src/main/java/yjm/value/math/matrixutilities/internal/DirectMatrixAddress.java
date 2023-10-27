package yjm.value.math.matrixutilities.internal;

import yjm.value.lang.exceptions.LibraryException;

import java.util.EnumSet;
import java.util.Set;

public class DirectMatrixAddress extends DirectAddress implements Address.MatrixAddress {

    /**
     * 构造方法
     *
     * @Author  Jiaming Yan
     */
    public DirectMatrixAddress(
            final double[] data,
            final int row0, final int row1,
            final Address chain,
            final int col0, final int col1,
            final Set<Flags> flags,
            final boolean contiguous,
            final int rows, final int cols) {
        super(data, row0, row1, chain, col0, col1, flags, contiguous, rows, cols);
    }

    @Override
    public MatrixAddress toFortran() {
        return isFortran() ? this :
                new DirectMatrixAddress(data, row0, row1, this.chain, col0, col1, EnumSet.of(Address.Flags.FORTRAN), contiguous, rows, cols);
    }

    @Override
    public MatrixAddress toJava() {
        return isFortran() ?
                new DirectMatrixAddress(data, row0+1, row1+1, this.chain, col0+1, col1+1, EnumSet.noneOf(Address.Flags.class), contiguous, rows, cols)
                : this;
    }

    @Override
    public MatrixOffset offset() {
        return new DirectMatrixAddressOffset(offset, offset);
    }

    @Override
    public MatrixOffset offset(final int row, final int col) {
        return new DirectMatrixAddressOffset(row, col);
    }

    @Override
    public int op(final int row, final int col) {
        return (row0+row)*cols + (col0+col);
    }

    @Override
    public DirectMatrixAddress clone() {
        try {
            return (DirectMatrixAddress) super.clone();
        } catch (final Exception e) {
            throw new LibraryException(e);
        }
    }

    private class DirectMatrixAddressOffset extends DirectAddressOffset implements Address.MatrixAddress.MatrixOffset {

        public DirectMatrixAddressOffset(final int row, final int col) {
            super.row = row0+row;
            super.col = col0+col;
        }

        @Override
        public void nextRow() {
            super.row++;
        }

        @Override
        public void nextCol() {
            super.col++;
        }

        @Override
        public void prevRow() {
            super.row--;
        }

        @Override
        public void prevCol() {
            super.col--;
        }

        @Override
        public void setRow(final int row) {
            super.row = row0+row;
        }

        @Override
        public void setCol(final int col) {
            super.col = col0+col;
        }

        @Override
        public int op() {
            return row*cols + col;
        }

    }

}
