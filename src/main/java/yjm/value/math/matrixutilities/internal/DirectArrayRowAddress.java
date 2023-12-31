package yjm.value.math.matrixutilities.internal;

import yjm.value.lang.exceptions.LibraryException;

import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @Author  Jiaming Yan
 * @Description DirectArrayRowAddress类
 */
public class DirectArrayRowAddress extends DirectAddress implements Address.ArrayAddress{

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    public DirectArrayRowAddress(
            final double[] data,
            final int row,
            final Address chain,
            final int col0,
            final int col1,
            final Set<Flags> flags,
            final boolean contiguous,
            final int rows,
            final int cols) {
        super(data, row, row+1, chain, col0, col1, flags, contiguous, rows, cols);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 若不是Fortan连续，返回一个Fortan连续的对象
     */
    @Override
    public ArrayAddress toFortran() {
        return isFortran() ? this :
                new DirectArrayRowAddress(data, row0, this.chain, col0, col1, EnumSet.of(Address.Flags.FORTRAN), contiguous, rows, cols);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 若是Fortan连续，返回不是Fortan连续的对象
     */
    @Override
    public ArrayAddress toJava() {
        return isFortran() ?
                new DirectArrayRowAddress(data, row0+1, this.chain, col0+1, col1+1, EnumSet.noneOf(Address.Flags.class), contiguous, rows, cols)
                : this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description
     */
    @Override
    public ArrayOffset offset() {
        return new DirectArrayRowAddressOffset(offset, offset);
    }

    @Override
    public ArrayOffset offset(final int index) {
        return new DirectArrayRowAddressOffset(offset, index);
    }

    @Override
    public int op(final int index) {
        return (row0 + offset) * cols + (col0 + index);
    }


    @Override
    public DirectArrayRowAddress clone() {
        try {
            return (DirectArrayRowAddress) super.clone();
        } catch (final Exception e) {
            throw new LibraryException(e);
        }
    }

    private class DirectArrayRowAddressOffset extends DirectAddressOffset implements Address.ArrayAddress.ArrayOffset {

        public DirectArrayRowAddressOffset(final int row, final int col) {
            super.row = row0 + row;
            super.col = col0 + col;
        }

        // implements Offset

        @Override
        public int op() {
            return row * cols + col;
        }

        // implements ArrayOffset

        @Override
        public void setIndex(final int index) {
            super.col = col0 + index;
        }


        // implements ListIterator

        @Override
        public void add(final Double e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int nextIndex() {
            return super.col == cols ? cols : super.col++;
        }

        @Override
        public int previousIndex() {
            return super.col == -1 ? -1 : super.col--;
        }

        @Override
        public boolean hasNext() {
            return super.col < col1;
        }

        @Override
        public boolean hasPrevious() {
            return super.col > -1;
        }

        @Override
        public Double next() {
            final int idx = op();
            nextIndex();
            if (idx==col1) throw new NoSuchElementException();
            return data[idx];
        }

        @Override
        public Double previous() {
            final int idx = previousIndex();
            if (idx==-1) throw new NoSuchElementException();
            return data[op()];
        }

        @Override
        public void set(final Double e) {
            final int idx = op();
            if ((idx==-1)||(idx==cols)) throw new IllegalStateException();
            data[idx] = e;
        }
    }
}
