package org.yjm.math.matrixutilities;

import org.yjm.QL;
import org.yjm.math.matrixutilities.internal.Address;
import org.yjm.lang.LibraryException;
import java.util.Set;


public abstract class Cells<T extends Address> implements Cloneable  {
    private final static String FORTRAN_ADDRESSING_EXPECTED = "variable \"%s\" should be FORTRAN-style addressing";
    protected final static String INVALID_ARGUMENTS = "invalid arguments";
    protected final static String WRONG_BUFFER_LENGTH = "wrong buffer length";
    protected final static String MATRIX_IS_INCOMPATIBLE = "matrix is incompatible";
    protected final static String ARRAY_IS_INCOMPATIBLE = "array is incompatible";
    protected final static String ITERATOR_IS_INCOMPATIBLE = "iterator is incompatible";
    protected final static String NOT_ENOUGH_STORAGE = "not enough storage area for operation";
    protected final static String MATRIX_MUST_BE_SQUARE = "matrix must be square";
    protected final static String MATRIX_MUST_BE_SYMMETRIC = "matrix must be symmetric";
    protected final static String MATRIX_IS_SINGULAR = "matrix is singular";
    protected final static String NON_CONTIGUOUS_DATA = "Operation not supported on non-contiguous data";

    protected final int rows;
    protected final int cols;
    protected final int size;

    protected T addr;

    public double[] $;

    protected Cells(
            final int rows,
            final int cols,
            final T addr) {
        this.rows = rows;
        this.cols = cols;
        this.addr = addr;
        this.size = rows*cols;
        this.$ = new double[size];
    }

    protected Cells(
            final int rows,
            final int cols,
            final double data[],
            final T addr) {
        this.rows = rows;
        this.cols = cols;
        this.$ = data;
        this.addr = addr;
        this.size = rows*cols;
        if (data.length != addr.rows()*addr.cols())
            throw new IllegalArgumentException("declared dimension do not match underlying storage size");
    }

    public final int rows()       { return rows; }
    public final int columns()    { return cols; }
    public final int cols()       { return cols; }
    public final int size()       { return size; }
    public final boolean empty() { return size <= 0; }

    public final Set<Address.Flags> flags() {
        return addr.flags();
    }

    public void requireFlags(final Set<Address.Flags> required, final String variable) {
        if (required.contains(Address.Flags.FORTRAN) != addr.isFortran()) {
            final String name = (variable==null) ? "variable" : (this.getClass().getSimpleName() + " " + variable);
            final String message = String.format(FORTRAN_ADDRESSING_EXPECTED, name);
            QL.error(String.format(FORTRAN_ADDRESSING_EXPECTED, name));
        }
    }

    public Cells clone() {
        try {
            return (Cells) super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new LibraryException(e);
        }
    }

}

