package org.yjm.math.matrixutilities;

import org.yjm.QL;
import org.yjm.math.matrixutilities.internal.Address;
import org.yjm.math.matrixutilities.internal.DirectMatrixAddress;

import java.util.EnumSet;
import java.util.Set;

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

    public Matrix(final int rows, final int cols) {
        this(rows, cols, EnumSet.noneOf(Address.Flags.class));
    }

    public Matrix(final int rows, final int cols, final Set<Address.Flags> flags) {
        super(rows, cols, null);
        this.addr = new DirectMatrixAddress(this.$, 0, rows, null, 0, cols, flags, true, rows, cols);
    }

    public Matrix(final double[][] data) {
        this(data, EnumSet.noneOf(Address.Flags.class));
    }

    public Matrix(final double[][] data, final Set<Address.Flags> flags) {
        super(data.length, data[0].length, null);
        this.addr = new DirectMatrixAddress(this.$, 0, data.length, null, 0, data[0].length, flags, true, data.length, data[0].length);
        for (int row=0; row<data.length; row++) {
            System.arraycopy(data[row], 0, this.$, row*this.cols, this.cols);
        }
    }

    public Matrix(final Matrix m) {
        super(m.rows(), m.cols(), copyData(m), m.addr.clone());
    }

    private static final double[] copyData(final Matrix m) {
        final int size = m.rows() * m.cols();
        final double[] data = new double[size];
        if (m.addr.isContiguous()) {
            System.arraycopy(m.$, 0, data, 0, size);
        } else {
            //FIXME: this code is probably wrong
            final MatrixOffset offset = m.addr.offset();
            final int cols = m.cols();
            for (int row = 0; row < m.rows(); row++) {
                System.arraycopy(m.$, offset.op(), data, row * cols, cols);
                offset.nextRow();
            }
        }
        return data;
    }

    public Matrix(
        final int rows,
        final int cols,
        final double[] data,
        final Address.MatrixAddress addr) {
            super(rows, cols, data, addr);
        }

    public int _(final int row, final int col) {
        return addr.op(row, col);
    }

    public double get(final int row, final int col) {
        return this.$[addr.op(row, col)];
    }

    public void set(final int row, final int col, final double value) {
        this.$[addr.op(row, col)] = value;
    }

    public Matrix addAssign(final Matrix another) {
        QL.require(rows() == another.rows() && cols() == another.cols() ,  MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
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

    public Matrix negative() {
        return mulAssign(-1);
    }

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
            result.$[result._(row)] = sum;
        }
        return result;
    }

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





    }
