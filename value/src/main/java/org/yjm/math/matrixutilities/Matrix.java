package org.yjm.math.matrixutilities;

import org.yjm.QL;
import org.yjm.math.matrixutilities.internal.*;
import org.yjm.math.matrixutilities.internal.Address.MatrixAddress;
import org.yjm.math.matrixutilities.internal.Address.MatrixAddress.MatrixOffset;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 * @Author  Jiaming Yan
 * @Description 矩阵类
 */
public class Matrix extends Cells<Address.MatrixAddress> implements Cloneable {

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    public Matrix() {
        this(EnumSet.noneOf(Address.Flags.class));
    }

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    public Matrix(final Set<Address.Flags> flags) {
        super(1, 1, null);
        super.addr = new DirectMatrixAddress(this.$, 0, 1, null, 0, 1, flags, true, 1, 1);
    }
    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    public Matrix(final int rows, final int cols) {
        this(rows, cols, EnumSet.noneOf(Address.Flags.class));
    }
    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    public Matrix(final int rows, final int cols, final Set<Address.Flags> flags) {
        super(rows, cols, null);
        this.addr = new DirectMatrixAddress(this.$, 0, rows, null, 0, cols, flags, true, rows, cols);
    }
    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    public Matrix(final double[][] data) {
        this(data, EnumSet.noneOf(Address.Flags.class));
    }
    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    public Matrix(final double[][] data, final Set<Address.Flags> flags) {
        super(data.length, data[0].length, null);
        this.addr = new DirectMatrixAddress(this.$, 0, data.length, null, 0, data[0].length, flags, true, data.length, data[0].length);
        for (int row=0; row<data.length; row++) {
            System.arraycopy(data[row], 0, this.$, row*this.cols, this.cols);
        }
    }
    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    public Matrix(final Matrix m) {
        super(m.rows(), m.cols(), copyData(m), m.addr.clone());
    }

    /**
     * @Author  Jiaming Yan
     * @Description 复制数据
     */
    private static final double[] copyData(final Matrix m) {
        final int size = m.rows() * m.cols();
        final double[] data = new double[size];
        if (m.addr.isContiguous()) {
            System.arraycopy(m.$, 0, data, 0, size);
        } else {
            final MatrixOffset offset = m.addr.offset();
            final int cols = m.cols();
            for (int row = 0; row < m.rows(); row++) {
                System.arraycopy(m.$, offset.op(), data, row * cols, cols);
                offset.nextRow();
            }
        }
        return data;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    public Matrix(
        final int rows,
        final int cols,
        final double[] data,
        final Address.MatrixAddress addr) {
            super(rows, cols, data, addr);
        }

    /**
     * @Author  Jiaming Yan
     * @Description 返回index
     */
    public int index(final int row, final int col) {
        return addr.op(row, col);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回index的值
     */
    public double get(final int row, final int col) {
        return this.$[addr.op(row, col)];
    }

    /**
     * @Author  Jiaming Yan
     * @Description 设置index的值
     */
    public void set(final int row, final int col, final double value) {
        this.$[addr.op(row, col)] = value;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 加法
     */
    public Matrix addAssign(final Matrix another) {
        QL.require(rows() == another.rows() && cols() == another.cols() ,  MATRIX_IS_INCOMPATIBLE);
        if (this.addr.isContiguous() && another.addr.isContiguous()) {
            for (int i=0; i<size(); i++) {
                this.$[i] += another.$[i];
            }
        } else {
            int addr = 0;
            final Address.MatrixAddress.MatrixOffset toff = this.addr.offset();
            final Address.MatrixAddress.MatrixOffset aoff = another.addr.offset();
            for (int row=0; row<rows(); row++) {
                toff.setRow(row);
                aoff.setRow(row);
                for (int col=0; col<cols(); col++) {
                    this.$[toff.op()] += another.$[aoff.op()];
                    addr++;
                    toff.nextCol();
                    aoff.nextCol();
                }
            }
        }
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 减法
     */
    public Matrix subAssign(final Matrix another) {
        QL.require(rows() == another.rows() && cols() == another.cols() ,  MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        if (this.addr.isContiguous() && another.addr.isContiguous()) {
            for (int i=0; i<size(); i++) {
                this.$[i] -= another.$[i];
            }
        } else {
            int addr = 0;
            final Address.MatrixAddress.MatrixOffset toff = this.addr.offset();
            final Address.MatrixAddress.MatrixOffset aoff = another.addr.offset();
            for (int row=0; row<rows(); row++) {
                toff.setRow(row);
                aoff.setRow(row);
                for (int col=0; col<cols(); col++) {
                    this.$[toff.op()] -= another.$[aoff.op()];
                    addr++;
                    toff.nextCol();
                    aoff.nextCol();
                }
            }
        }
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 乘法
     */
    public Matrix mulAssign(final double scalar) {
        if (addr.isContiguous()) {
            for (int addr=0; addr<size(); addr++) {
                $[addr] *= scalar;
            }
        } else {
            final Address.MatrixAddress.MatrixOffset dst = this.addr.offset();
            for (int row = 0; row < rows(); row++) {
                dst.setRow(row);
                for (int col = 0; col < cols(); col++) {
                    $[dst.op()] *= scalar;
                    dst.nextCol();
                }
            }
        }
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 除法
     */
    public Matrix divAssign(final double scalar) {
        if (addr.isContiguous()) {
            for (int addr=0; addr<size(); addr++) {
                $[addr] /= scalar;
            }
        } else {
            final Address.MatrixAddress.MatrixOffset dst = this.addr.offset();
            for (int row = 0; row < rows(); row++) {
                dst.setRow(row);
                for (int col = 0; col < cols(); col++) {
                    $[dst.op()] /= scalar;
                    dst.nextCol();
                }
            }
        }
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 加法
     */
    public Matrix add(final Matrix another) {
        QL.require(rows() == another.rows() && cols() == another.cols() ,  MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Matrix result = new Matrix(rows(), cols());
        if (this.addr.isContiguous() && another.addr.isContiguous()) {
            for (int i=0; i<size(); i++) {
                result.$[i] = this.$[i] + another.$[i];
            }
        } else {
            int addr = 0;
            final Address.MatrixAddress.MatrixOffset toff = this.addr.offset();
            final Address.MatrixAddress.MatrixOffset aoff = another.addr.offset();
            for (int row=0; row<rows(); row++) {
                toff.setRow(row);
                aoff.setRow(row);
                for (int col=0; col<cols(); col++) {
                    result.$[addr] = this.$[toff.op()] + another.$[aoff.op()];
                    addr++;
                    toff.nextCol();
                    aoff.nextCol();
                }
            }
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 减法
     */
    public Matrix sub(final Matrix another) {
        QL.require(rows() == another.rows() && cols() == another.cols() ,  MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Matrix result = new Matrix(rows(), cols());
        if (this.addr.isContiguous() && another.addr.isContiguous()) {
            for (int addr=0; addr<size(); addr++) {
                result.$[addr] = this.$[addr] - another.$[addr];
            }
        } else {
            int addr = 0;
            final Address.MatrixAddress.MatrixOffset toff = this.addr.offset();
            final Address.MatrixAddress.MatrixOffset aoff = another.addr.offset();
            for (int row=0; row<rows(); row++) {
                toff.setRow(row);
                aoff.setRow(row);
                for (int col=0; col<cols(); col++) {
                    result.$[addr] = this.$[toff.op()] - another.$[aoff.op()];
                    addr++;
                    toff.nextCol();
                    aoff.nextCol();
                }
            }
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 取负
     */
    public Matrix negative() {
        return mulAssign(-1);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 乘法
     */
    public Matrix mul(final double scalar) {
        final Matrix result = new Matrix(rows(), cols());
        if (addr.isContiguous()) {
            for (int addr=0; addr<size(); addr++) {
                result.$[addr] = this.$[addr] * scalar;
            }
        } else {
            int addr = 0;
            final Address.MatrixAddress.MatrixOffset src = this.addr.offset();
            for (int row = 0; row < rows(); row++) {
                src.setRow(row);
                for (int col = 0; col < cols(); col++) {
                    result.$[addr] = this.$[src.op()] * scalar;
                    addr++;
                    src.nextCol();
                }
            }
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 除法
     */
    public Matrix div(final double scalar) {
        final Matrix result = new Matrix(rows(), cols());
        if (addr.isContiguous()) {
            for (int addr=0; addr<size(); addr++) {
                result.$[addr] = this.$[addr] / scalar;
            }
        } else {
            int addr = 0;
            final Address.MatrixAddress.MatrixOffset src = this.addr.offset();
            for (int row = 0; row < rows(); row++) {
                src.setRow(row);
                for (int col = 0; col < cols(); col++) {
                    result.$[addr] = this.$[src.op()] / scalar;
                    addr++;
                    src.nextCol();
                }
            }
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 乘法
     */
    public Array mul(final Array array) {
        QL.require(cols() == array.size(), ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Array result = new Array(rows(), this.flags());
        final Address.MatrixAddress.MatrixOffset toff = this.addr.offset();
        final Address.ArrayAddress.ArrayOffset  aoff = array.addr.offset();
        final int offsetT = this.addr.isFortran() ? 1 : 0;
        final int offsetA = array.addr.isFortran() ? 1 : 0;
        for (int row = offsetT; row < result.size()+offsetT; row++) {
            toff.setRow(row); toff.setCol(offsetT);
            aoff.setIndex(offsetA);
            double sum = 0.0;
            for (int col = offsetT; col < this.cols()+offsetT; col++) {
                final double telem = this.$[toff.op()];
                final double aelem = array.$[aoff.op()];
                sum += telem * aelem;
                toff.nextCol();
                aoff.nextIndex();
            }
            result.$[result.index(row)] = sum;
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 乘法
     */
    public Matrix mul(final Matrix another) {
        QL.require(cols() == another.rows(),  MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Matrix result = new Matrix(rows(), another.cols(), this.flags());
        final Address.MatrixAddress.MatrixOffset toff = this.addr.offset();
        final Address.MatrixAddress.MatrixOffset aoff = another.addr.offset();
        final int offsetT = this.addr.isFortran() ? 1 : 0;
        final int offsetA = another.addr.isFortran() ? 1 : 0;
        for (int col = offsetA; col < another.cols()+offsetA; col++) {
            for (int row = offsetT; row < this.rows()+offsetT; row++) {
                toff.setRow(row); toff.setCol(offsetT);
                aoff.setRow(offsetA); aoff.setCol(col);
                double sum = 0.0;
                for (int i = 0; i < this.cols(); i++) {
                    final double telem = this.$[toff.op()];
                    final double aelem = another.$[aoff.op()];
                    sum += telem * aelem;
                    toff.nextCol();
                    aoff.nextRow();
                }
                result.$[result.addr.op(row, col-offsetA+offsetT)] = sum;
            }
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description LU分解
     */
    public LUDecomposition lu() {
        return new LUDecomposition(this);
    }

    /**
     * @Author  Jiaming Yan
     * @Description QR分解
     */
//    public QRDecomposition qr() {return new QRDecomposition(this);}

    /**
     * @Author  Jiaming Yan
     * @Description QR分解
     */
//    public QRDecomposition qr(final boolean pivot) {
//        return new QRDecomposition(this, pivot);
//    }

    /**
     * @Author  Jiaming Yan
     * @Description Cholesky分解
     */
    public CholeskyDecomposition cholesky() {
        return new CholeskyDecomposition(this);
    }

    /**
     * @Author  Jiaming Yan
     * @Description Schur分解
     */
//    public SymmetricSchurDecomposition schur() {
//        return new SymmetricSchurDecomposition(this);
//    }

    /**
     * @Author  Jiaming Yan
     * @Description 奇异值分解
     */
//    public SVD svd() {
//        return new SVD(this);
//    }

    /**
     * @Author  Jiaming Yan
     * @Description 特征值分解
     */
//    public EigenvalueDecomposition eigenvalue() {
//        return new EigenvalueDecomposition(this);
//    }

    /**
     * @Author  Jiaming Yan
     * @Description 转置
     */
    public Matrix transpose() {
        final int offset = addr.isFortran() ? 1 : 0;
        final Matrix result = new Matrix(cols(), rows(), this.flags());
        final Address.MatrixAddress.MatrixOffset src = this.addr.offset(offset, offset);
        final Address.MatrixAddress.MatrixOffset dst = result.addr.offset(offset, offset);
        for (int row=offset; row<rows()+offset; row++) {
            src.setRow(row); src.setCol(offset);
            dst.setRow(offset);   dst.setCol(row);
            for (int col=offset; col<cols()+offset; col++) {
                result.$[dst.op()] = this.$[src.op()];
                src.nextCol();
                dst.nextRow();
            }
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 对角线
     */
    public Array diagonal() {
        QL.require(rows() == cols(),  MATRIX_MUST_BE_SQUARE);
        final Array result = new Array(cols());
        int addr = 0;
        for (int i = 0; i < cols(); i++) {
            result.$[i] = $[addr];
            addr += cols() + 1;
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 矩阵行列式
     */
    public double determinant() {
        return new LUDecomposition(this).det();
    }

    /**
     * @Author  Jiaming Yan
     * @Description 逆矩阵
     */
    public Matrix inverse() {
        QL.require(this.rows == this.cols, "matrix is not square");
        return (new LUDecomposition(this)).solve(new Identity(rows()));
    }


    public Array rangeRow(final int row) {
        return rangeRow(row, 0, cols());
    }

    public Array rangeRow(final int row, final int col0) {
        return rangeRow(row, col0, cols());
    }

    public Array rangeRow(final int row, final int col0, final int col1) {
        final int offset = addr.isFortran() ? 1 : 0;
        QL.require(row  >= offset && row  <= rows()+offset, ArrayIndexOutOfBoundsException.class, Address.INVALID_ROW_INDEX);
        QL.require(col0 >= offset && col0 < cols()+offset && col1 >= offset && col1 <= cols()+offset, ArrayIndexOutOfBoundsException.class, Address.INVALID_COLUMN_INDEX);
        QL.require(col0 <= col1, ArrayIndexOutOfBoundsException.class, Address.INVALID_BACKWARD_INDEXING);
        return new RangeRow(row-offset, this.addr, col0-offset, col1-offset, $, rows(), cols());
    }

    public Array rangeCol(final int col) {
        return rangeCol(col, 0, rows());
    }

    public Array rangeCol(final int col, final int row0) {
        return rangeCol(col, row0, rows());
    }

    public Array rangeCol(final int col, final int row0, final int row1) {
        final int offset = addr.isFortran() ? 1 : 0;
        QL.require(col  >= offset && col  <= cols()+offset, ArrayIndexOutOfBoundsException.class, Address.INVALID_COLUMN_INDEX);
        QL.require(row0 >= offset && row0 < rows()+offset && row1 >= offset && row1 <= rows()+offset, ArrayIndexOutOfBoundsException.class, Address.INVALID_ROW_INDEX);
        QL.require(row0 <= row1, ArrayIndexOutOfBoundsException.class, Address.INVALID_BACKWARD_INDEXING);
        return new RangeCol(row0-offset, row1-offset, this.addr, col-offset, $, rows(), cols());
    }

    public Matrix range(final int row0, final int row1, final int col0, final int col1) {
        final int offset = addr.isFortran() ? 1 : 0;
        QL.require(row0 >= offset && row0 < rows()+offset && row1 >= offset && row1 <= rows()+offset, ArrayIndexOutOfBoundsException.class, Address.INVALID_ROW_INDEX);
        QL.require(row0<=row1, ArrayIndexOutOfBoundsException.class, Address.INVALID_BACKWARD_INDEXING);
        QL.require(col0 >= offset && col0 < cols()+offset && col1 >= offset && col1 <= cols()+offset, ArrayIndexOutOfBoundsException.class, Address.INVALID_COLUMN_INDEX);
        QL.require(col0<=col1, ArrayIndexOutOfBoundsException.class, Address.INVALID_BACKWARD_INDEXING);
        final boolean contiguous = super.cols()==(col1-col0+1);
        return new RangeMatrix(row0-offset, row1-offset, this.addr, col0-offset, col1-offset, contiguous, $, rows(), cols());
    }

    public Matrix range(final int[] ridx, final int col0, final int col1) {
        return new RangeMatrix(ridx, this.addr, col0, col1, $, rows(), cols());
    }

    public Matrix range(final int row0, final int row1, final int[] cidx) {
        return new RangeMatrix(row0, row1, this.addr, cidx, $, rows(), cols());
    }

    public Matrix range(final int[] ridx, final int[] cidx) {
        return new RangeMatrix(ridx, this.addr, cidx, $, rows(), cols());
    }

    public Array constRangeRow(final int row) {
        return constRangeRow(row, 0, cols());
    }

    public Array constRangeRow(final int row, final int col0) {
        return constRangeRow(row, col0, cols());
    }

    public Array constRangeRow(final int row, final int col0, final int col1) {
        final int offset = addr.isFortran() ? 1 : 0;
        QL.require(row  >= offset && row  < rows()+offset, ArrayIndexOutOfBoundsException.class, Address.INVALID_ROW_INDEX);
        QL.require(col0 >= offset && col0 < cols()+offset && col1 >= offset && col1 <= cols()+offset, ArrayIndexOutOfBoundsException.class, Address.INVALID_COLUMN_INDEX);
        QL.require(col0<=col1, ArrayIndexOutOfBoundsException.class, Address.INVALID_BACKWARD_INDEXING);
        return new ConstRangeRow(row, this.addr, col0, col1, $, rows(), cols());
    }


    public Array constRangeCol(final int col) {
        return constRangeCol(col, 0, rows());
    }

    public Array constRangeCol(final int col, final int row0) {
        return constRangeCol(col, row0, rows());
    }

    public Array constRangeCol(final int col, final int row0, final int row1) {
        final int offset = addr.isFortran() ? 1 : 0;
        QL.require(col  >= offset && col  < cols()+offset, ArrayIndexOutOfBoundsException.class, Address.INVALID_COLUMN_INDEX);
        QL.require(row0 >= offset && row0 < rows()+offset && row1 >= offset && row1 <= rows()+offset, ArrayIndexOutOfBoundsException.class, Address.INVALID_ROW_INDEX);
        QL.require(row0<=row1, ArrayIndexOutOfBoundsException.class, Address.INVALID_BACKWARD_INDEXING);
        return new ConstRangeCol(row0, row1, this.addr, col, $, rows(), cols());
    }


    public Matrix constRange(final int row0, final int row1, final int col0, final int col1) {
        final int offset = addr.isFortran() ? 1 : 0;
        QL.require(row0 >= offset && row0 < rows()+offset && row1 >= offset && row1 <= rows()+offset, ArrayIndexOutOfBoundsException.class, Address.INVALID_ROW_INDEX);
        QL.require(row0<=row1, ArrayIndexOutOfBoundsException.class, Address.INVALID_BACKWARD_INDEXING);
        QL.require(col0 >= offset && col0 < cols()+offset && col1 >= offset && col1 <= cols()+offset, ArrayIndexOutOfBoundsException.class, Address.INVALID_COLUMN_INDEX);
        QL.require(col0<=col1, ArrayIndexOutOfBoundsException.class, Address.INVALID_BACKWARD_INDEXING);
        final boolean contiguous = super.cols()==(col1-col0+1);
        return new ConstRangeMatrix(row0, row1, this.addr, col0, col1, contiguous, $, rows(), cols());
    }

    public Matrix constRange(final int[] ridx, final int col0, final int col1) {
        return new ConstRangeMatrix(ridx, this.addr, col0, col1, $, rows(), cols());
    }

    public Matrix constRange(final int row0, final int row1, final int[] cidx) {
        return new ConstRangeMatrix(row0, row1, this.addr, cidx, $, rows(), cols());
    }

    public Matrix constRange(final int[] ridx, final int[] cidx) {
        return new ConstRangeMatrix(ridx, this.addr, cidx, $, rows(), cols());
    }

    public Matrix toFortran() {
        return this.addr.isFortran()
                ?  this
                : new Matrix(this.rows, this.cols, this.$, this.addr.toFortran());
    }

    public Matrix toJava() {
        return this.addr.isFortran()
                ?  new Matrix(this.rows, this.cols, this.$, this.addr.toJava())
                : this;
    }

    public Matrix fill(final double scalar) {
        QL.require(addr.isContiguous(), NON_CONTIGUOUS_DATA);
        Arrays.fill($, addr.base(), this.size(), scalar);
        return this;
    }

    public Matrix fill(final Matrix another) {
        QL.require(addr.isContiguous(), NON_CONTIGUOUS_DATA);
        QL.require(another.addr.isContiguous(), NON_CONTIGUOUS_DATA);
        QL.require(this.rows()==another.rows() && this.cols()==another.cols() && this.size()==another.size(), WRONG_BUFFER_LENGTH);
        System.arraycopy(another.$, another.addr.base(), this.$, this.addr.base(), this.size());
        return this;
    }

    public void fillRow(final int row, final Array array) {
        QL.require(cols() == array.size() ,  ARRAY_IS_INCOMPATIBLE);
        if (this.addr.isContiguous() && array.addr.isContiguous()) {
            System.arraycopy(array.$, 0, $, addr.op(row, 0), cols());
        } else {
            final Address.ArrayAddress.ArrayOffset src = array.addr.offset();
            final Address.MatrixAddress.MatrixOffset dst = this.addr.offset(row, 0);
            for (int col = 0; col < cols(); col++) {
                $[dst.op()] = array.$[src.op()];
                src.nextIndex();
                dst.nextCol();
            }
        }
    }

    public void fillCol(final int col, final Array array) {
        QL.require(rows() == array.size() ,  ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        if (this.addr.isContiguous() && array.addr.isContiguous() && cols() == 1) {
            System.arraycopy(array.$, 0, $, 0, size());
        } else {
            final Address.ArrayAddress.ArrayOffset src = array.addr.offset();
            final Address.MatrixAddress.MatrixOffset dst = this.addr.offset(0, col);
            for (int row = 0; row < rows(); row++) {
                $[dst.op()] = array.$[src.op()];
                src.nextIndex();
                dst.nextRow();
            }
        }
    }

    public Matrix swap(final Matrix another) {
        QL.require(addr.isContiguous(), NON_CONTIGUOUS_DATA);
        QL.require(another.addr.isContiguous(), NON_CONTIGUOUS_DATA);
        QL.require(this.rows()==another.rows() && this.cols()==another.cols() && this.size()==another.size(), WRONG_BUFFER_LENGTH);
        // swaps data
        final double [] tdata;
        final Address.MatrixAddress taddr;
        tdata = this.$;  this.$ = another.$;  another.$ = tdata;
        taddr = this.addr;  this.addr = another.addr;  another.addr = taddr;
        return this;
    }

    public Matrix sort() {
        QL.require(addr.isContiguous(), NON_CONTIGUOUS_DATA);
        Arrays.sort($, addr.base(), addr.last());
        return this;
    }

    @Override
    public Matrix clone() {
        final Matrix clone = (Matrix)super.clone();
        clone.$ = copyData(this);
        clone.addr = new DirectMatrixAddress(clone.$, 0, this.rows, null, 0, this.cols, this.flags(), true, this.rows, this.cols);
        return clone;
    }

    public boolean equals(final Object obj) {
        return super.equals(obj);}

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        final int offset = addr.isFortran() ? 1 : 0;
        final StringBuffer sb = new StringBuffer();
        sb.append("[rows=").append(rows()).append(" cols=").append(cols()).append(" addr=").append(addr).append('\n');
        for (int row = offset; row < rows()+offset; row++) {
            sb.append("  [ ");
            sb.append($[addr.op(row, offset)]);
            for (int col = 1+offset; col < cols()+offset; col++) {
                sb.append(", ");
                sb.append($[addr.op(row, col)]);
            }
            sb.append("  ]\n");
        }
        sb.append("]\n");
        return sb.toString();
    }

    public int offset() {
        return addr.isFortran() ? 1 : 0;
    }

    private static class RangeRow extends Array {

        public RangeRow(
                final int row,
                final Address.MatrixAddress chain,
                final int col0,
                final int col1,
                final double[] data,
                final int rows,
                final int cols) {
            super(1, col1-col0,
                    data,
                    new DirectArrayRowAddress(data, row, chain, col0, col1, null, true, rows, cols));
        }
    }

    private static class RangeCol extends Array {

        public RangeCol(
                final int row0,
                final int row1,
                final Address.MatrixAddress chain,
                final int col,
                final double[] data,
                final int rows,
                final int cols) {
            super(row1-row0, 1,
                    data,
                    new DirectArrayColAddress(data, row0, row1, chain, col, null, true, rows, cols));
        }
    }

    private static class RangeMatrix extends Matrix {

        public RangeMatrix(
                final int row0,
                final int row1,
                final Address.MatrixAddress chain,
                final int col0,
                final int col1,
                final boolean contiguous,
                final double[] data,
                final int rows,
                final int cols) {
            super(row1-row0, col1-col0,
                    data,
                    new DirectMatrixAddress(data, row0, row1, chain, col0, col1, null, true, rows, cols));
        }

        public RangeMatrix(
                final int[] ridx,
                final Address.MatrixAddress chain,
                final int col0,
                final int col1,
                final double[] data,
                final int rows,
                final int cols) {
            super(ridx.length, col1-col0,
                    data,
                    new MappedMatrixAddress(data, ridx, chain, col0, col1, null, true, rows, cols));
        }

        public RangeMatrix(
                final int row0,
                final int row1,
                final Address.MatrixAddress chain,
                final int[] cidx,
                final double[] data,
                final int rows,
                final int cols) {
            super(row1-row0, cidx.length,
                    data,
                    new MappedMatrixAddress(data, row0, row1, chain, cidx, null, true, rows, cols));
        }

        public RangeMatrix(
                final int[] ridx,
                final Address.MatrixAddress chain,
                final int[] cidx,
                final double[] data,
                final int rows,
                final int cols) {
            super(ridx.length, cidx.length,
                    data,
                    new MappedMatrixAddress(data, ridx, chain, cidx, null, true, rows, cols));
        }
    }

    private static class ConstRangeRow extends RangeRow {

        public ConstRangeRow(
                final int row,
                final Address.MatrixAddress chain,
                final int col0,
                final int col1,
                final double[] data,
                final int rows,
                final int cols) {
            super(row, chain, col0, col1, data, rows, cols);
        }

        @Override
        public void set(final int pos, final double value) {
            throw new UnsupportedOperationException();
        }
    }


    private static class ConstRangeCol extends RangeCol {

        public ConstRangeCol(
                final int row0,
                final int row1,
                final Address.MatrixAddress chain,
                final int col,
                final double[] data,
                final int rows,
                final int cols) {
            super(row0, row1, chain, col, data, rows, cols);
        }

        @Override
        public void set(final int pos, final double value) {
            throw new UnsupportedOperationException();
        }
    }

    private static class ConstRangeMatrix extends RangeMatrix {

        public ConstRangeMatrix(
                final int row0,
                final int row1,
                final Address.MatrixAddress chain,
                final int col0,
                final int col1,
                final boolean contiguous,
                final double[] data,
                final int rows,
                final int cols) {
            super(row0, row1, chain, col0, col1, contiguous, data, rows, cols);
        }

        public ConstRangeMatrix(
                final int[] ridx,
                final Address.MatrixAddress chain,
                final int col0,
                final int col1,
                final double[] data,
                final int rows,
                final int cols) {
            super(ridx, chain, col0, col1, data, rows, cols);
        }

        public ConstRangeMatrix(
                final int row0,
                final int row1,
                final Address.MatrixAddress chain,
                final int[] cidx,
                final double[] data,
                final int rows,
                final int cols) {
            super(row0, row1, chain, cidx, data, rows, cols);
        }

        public ConstRangeMatrix(
                final int[] ridx,
                final Address.MatrixAddress chain,
                final int[] cidx,
                final double[] data,
                final int rows,
                final int cols) {
            super(ridx, chain, cidx, data, rows, cols);
        }

        @Override
        public void set(final int row, final int col, final double value) {
            throw new UnsupportedOperationException();
        }

    }

}
