package org.yjm.math.matrixutilities;

import org.yjm.QL;
import org.yjm.math.Ops;
import org.yjm.math.functions.LessThanPredicate;
import org.yjm.math.matrixutilities.internal.Address;
import org.yjm.math.matrixutilities.internal.DirectArrayRowAddress;

import java.util.*;
import java.util.Set;

/**
 * @Author  Jiaming Yan
 * @Description 一维数组
 */
public class Array extends Cells<Address.ArrayAddress> implements Cloneable, Iterable<Double>, Algebra<Array> {

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数，一个元素的数组
     */
    public Array() {
        this(0, EnumSet.noneOf(Address.Flags.class));
    }

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数，一个元素的数组
     */
    public Array(final Set<Address.Flags> flags) {
        super(1, 1, null);
        this.addr = new DirectArrayRowAddress(this.$, 0, null, 0, 0, flags, true, 1, 1);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数，一个元素的数组
     */
    public Array(final int size) {
        this(size, EnumSet.noneOf(Address.Flags.class));
    }

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    public Array(final int size, final Set<Address.Flags> flags) {
        super(1, size, null);
        this.addr = new DirectArrayRowAddress(this.$, 0, null, 0, size-1, flags, true, 1, size);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    public Array(final double[] array) {
        this(array, EnumSet.noneOf(Address.Flags.class));
    }

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    public Array(final double[] array, final Set<Address.Flags> flags) {
        super(1, array.length, null);
        this.addr = new DirectArrayRowAddress(this.$, 0, null, 0, array.length-1, flags, true, 1, array.length);
        System.arraycopy(array, 0, $, 0, this.size());
    }

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    public Array(final double[] array, final int size) {
        this(array, size, EnumSet.noneOf(Address.Flags.class));
    }

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    public Array(final double[] array, final int size, final Set<Address.Flags> flags) {
        super(1, size, null);
        this.addr = new DirectArrayRowAddress(this.$, 0, null, 0, size-1, flags, true, 1, size);
        System.arraycopy(array, 0, $, 0, this.size());
    }

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    public Array(final Array array) {
        this(array, EnumSet.noneOf(Address.Flags.class));
    }

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    public Array(final Array array, final Set<Address.Flags> flags) {
        super(1, array.size(), null);
        this.addr = new DirectArrayRowAddress(this.$, 0, null, 0, array.size(), array.flags(), true, 1, array.size());
        if (array.addr.isContiguous()) {
            final int begin = array.addr.col0()+(addr.isFortran() ? 1 : 0);
            System.arraycopy(array.$, begin, $, 0, this.size());
        } else {
            for (int i=0; i<array.size(); i++) {
                this.$[i] = array.get(i);
            }
        }
    }

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    protected Array(
            final int rows,
            final int cols,
            final double[] data,
            final Address.ArrayAddress addr) {
        super(rows, cols, data, addr);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 复制Array对象
     */
    @Override
    public Array clone() {
        final Array clone = (Array) super.clone();
        clone.$ = new double[this.size()];
        clone.addr = new DirectArrayRowAddress(clone.$, 0, null, 0, this.size(), this.flags(), true, 1, this.size());
        if (this.addr.isContiguous()) {
            final int begin = this.addr.col0() + (addr.isFortran() ? 1 : 0);
            System.arraycopy(this.$, begin, clone.$, 0, this.size());
        } else {
            for (int i=0; i<this.size(); i++) {
                clone.$[i] = this.get(i);
            }
        }
        return clone;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回起始元素地址
     */
    public int begin() {
        return addr.isFortran() ? 1 : 0;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回结束元素地址
     */
    public int end() {
        return size() + (addr.isFortran() ? 1 : 0);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回index值
     */
    public int index(final int index) {
        return addr.op(index);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回第一个值
     */
    public double first() {
        return $[index(addr.isFortran() ? 1 : 0)];
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回最后一个值
     */
    public double last() {
        return $[index(end() - 1)];
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回pos的值
     */
    public double get(final int pos) {
        return $[addr.op(pos)];
    }

    /**
     * @Author  Jiaming Yan
     * @Description 判断pos的值是否 = value
     */
    public void set(final int pos, final double value) {
        $[addr.op(pos)] = value;
    }


    /**
     * @Author  Jiaming Yan
     * @Description 加法
     */
    @Override
    public Array addAssign(final double scalar) {
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int i=0; i<size(); i++) {
            $[src.op()] += scalar;
            src.nextIndex();
        }
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 减法
     */
    @Override
    public Array subAssign(final double scalar) {
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int i=0; i<size(); i++) {
            $[src.op()] -= scalar;
            src.nextIndex();
        }
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 减法
     */
    public Array subAssign(final Array another) {
        QL.require(this.size() == another.size(), ARRAY_IS_INCOMPATIBLE);
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset();
        final Address.ArrayAddress.ArrayOffset aoff = another.addr.offset();
        for (int i=0; i<size(); i++) {
            $[toff.op()] -= another.$[aoff.op()];
            toff.nextIndex();
            aoff.nextIndex();
        }
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 乘法
     */
    @Override
    public Array mulAssign(final double scalar) {
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int i=0; i<size(); i++) {
            $[src.op()] *= scalar;
            src.nextIndex();
        }
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 除法
     */
    @Override
    public Array divAssign(final double scalar) {
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int i = 0; i < size(); i++) {
            $[src.op()] /= scalar;
            src.nextIndex();
        }
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 加法
     */
    public Array addAssign(final Array another) {
        QL.require(this.size() == another.size(), ARRAY_IS_INCOMPATIBLE); //
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset();
        final Address.ArrayAddress.ArrayOffset aoff = another.addr.offset();
        for (int i=0; i<size(); i++) {
            $[toff.op()] += another.$[aoff.op()];
            toff.nextIndex();
            aoff.nextIndex();
        }
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 乘法
     */
    @Override
    public Array mulAssign(final Array another) {
        QL.require(this.size() == another.size(), ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset();
        final Address.ArrayAddress.ArrayOffset aoff = another.addr.offset();
        for (int i=0; i<size(); i++) {
            $[toff.op()] *= another.$[aoff.op()];
            toff.nextIndex();
            aoff.nextIndex();
        }
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 除法
     */
    @Override
    public Array divAssign(final Array another) {
        QL.require(this.size() == another.size(), ARRAY_IS_INCOMPATIBLE); // QA:[RG]::verified
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset();
        final Address.ArrayAddress.ArrayOffset aoff = another.addr.offset();
        for (int i=0; i<size(); i++) {
            this.$[toff.op()] /= another.$[aoff.op()];
            toff.nextIndex();
            aoff.nextIndex();
        }
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 加法，生成新对象
     */
    @Override
    public Array add(final double scalar) {
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int col=0; col<size(); col++) {
            result.$[col] = $[src.op()] + scalar;
            src.nextIndex();
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 减法，生成新对象
     */
    @Override
    public Array sub(final double scalar) {
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int col=0; col<size(); col++) {
            result.$[col] = $[src.op()] - scalar;
            src.nextIndex();
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 乘法，生成新对象
     */
    @Override
    public Array mul(final double scalar) {
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int col=0; col<size(); col++) {
            result.$[col] = $[src.op()] * scalar;
            src.nextIndex();
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 生成负值对象
     */
    @Override
    public Array negative() {
        return mul(-1);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 除法，生成新对象
     */
    @Override
    public Array div(final double scalar) {
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int col=0; col<size(); col++) {
            result.$[col] = $[src.op()] / scalar;
            src.nextIndex();
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 加法，生成新对象
     */
    @Override
    public Array add(final Array another) {
        QL.require(this.size() == another.size(), MATRIX_IS_INCOMPATIBLE);
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset();
        final Address.ArrayAddress.ArrayOffset aoff = another.addr.offset();
        for (int col=0; col<size(); col++) {
            result.$[col] = $[toff.op()] + another.$[aoff.op()];
            toff.nextIndex();
            aoff.nextIndex();
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 减法，生成新对象
     */
    public Array sub(final Array another) {
        QL.require(this.size() == another.size(), MATRIX_IS_INCOMPATIBLE);
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset();
        final Address.ArrayAddress.ArrayOffset aoff = another.addr.offset();
        for (int col=0; col<size(); col++) {
            result.$[col] = $[toff.op()] - another.$[aoff.op()];
            toff.nextIndex();
            aoff.nextIndex();
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 乘法，生成新对象
     */
    @Override
    public Array mul(final Array another) {
        QL.require(this.size() == another.size(), MATRIX_IS_INCOMPATIBLE);
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset();
        final Address.ArrayAddress.ArrayOffset aoff = another.addr.offset();
        for (int col=0; col<size(); col++) {
            result.$[col] = $[toff.op()] * another.$[aoff.op()];
            toff.nextIndex();
            aoff.nextIndex();
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 除法，生成新对象
     */
    @Override
    public Array div(final Array another) {
        QL.require(this.size() == another.size(), MATRIX_IS_INCOMPATIBLE);
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset();
        final Address.ArrayAddress.ArrayOffset aoff = another.addr.offset();
        for (int col=0; col<size(); col++) {
            result.$[col] = $[toff.op()] / another.$[aoff.op()];
            toff.nextIndex();
            aoff.nextIndex();
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 乘法，生成新对象
     */
    @Override
    public Array mul(final Matrix matrix) {
        QL.require(this.size() == matrix.rows(), MATRIX_IS_INCOMPATIBLE);
        final Array result = new Array(matrix.cols());
        final Address.ArrayAddress.ArrayOffset  toff = this.addr.offset();
        final Address.MatrixAddress.MatrixOffset moff = matrix.addr.offset();
        final int offsetA = this.addr.isFortran() ? 1 : 0;
        final int offsetM = matrix.addr.isFortran() ? 1 : 0;
        for (int col=0; col<matrix.cols(); col++) {
            toff.setIndex(offsetA);
            moff.setRow(offsetM); moff.setCol(col+offsetM);
            double sum = 0.0;
            for (int row=0; row<matrix.rows(); row++) {
                final double telem = this.$[toff.op()];
                final double aelem = matrix.$[moff.op()];
                sum += telem * aelem;
                toff.nextIndex();
                moff.nextRow();
            }
            result.$[col] = sum;
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 取0和SIZE的最小值
     */
    @Override
    public double min() {
        return min(0, this.size());
    }

    /**
     * @Author  Jiaming Yan
     * @Description 取from到to的最小值
     */
    @Override
    public double min(final int from, final int to) {
        QL.require(from >= 0 && to > from && to <= size(),  INVALID_ARGUMENTS);
        final int offset = addr.isFortran() ? 1 : 0;
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset(from + offset);
        double result = $[src.op()];
        for (int i=0; i<(to-from); i++) {
            final double tmp = $[src.op()];
            src.nextIndex();
            if (tmp < result) {
                result = tmp;
            }
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 取from到to的最大值
     */
    @Override
    public double max() {
        return max(0, this.size());
    }

    /**
     * @Author  Jiaming Yan
     * @Description 取from到to的最大值
     */
    @Override
    public double max(final int from, final int to) {
        QL.require(from >= 0 && to > from && to <= size(),  INVALID_ARGUMENTS); // QA:[RG]::verified
        final int offset = addr.isFortran() ? 1 : 0;
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset(from+offset);
        double result = $[src.op()];
        for (int i=0; i<(to-from); i++) {
            final double tmp = $[src.op()];
            src.nextIndex();
            if (tmp > result) {
                result = tmp;
            }
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 绝对值
     */
    @Override
    public Array abs() {
        final Array result = new Array(this.size(), this.addr.flags());
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        final int offset = addr.isFortran() ? 1 : 0;
        for (int i=offset; i<this.size()+offset; i++) {
            result.$[result.index(i)] = Math.abs($[src.op()]);
            src.nextIndex();
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 平方
     */
    @Override
    public Array sqr() {
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int i=0; i<this.size(); i++) {
            final double a = $[src.op()];
            result.$[i] = a*a;
            src.nextIndex();
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 开根号
     */
    @Override
    public Array sqrt() {
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int i=0; i<this.size(); i++) {
            result.$[i] = Math.sqrt($[src.op()]);
            src.nextIndex();
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description ln
     */
    @Override
    public Array log() {
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int i=0; i<this.size(); i++) {
            result.$[i] = Math.log($[src.op()]);
            src.nextIndex();
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 指数
     */
    @Override
    public Array exp() {
        final Array result = new Array(this.size());
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset();
        for (int i=0; i<this.size(); i++) {
            result.$[i] = Math.exp($[src.op()]);
            src.nextIndex();
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 点积
     */
    @Override
    public double dotProduct(final Array another) {
        final int offset = another.addr.isFortran() ? 1 : 0;
        return dotProduct(another, offset, another.size()+offset);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 点积
     */
    @Override
    public double dotProduct(final Array another, final int from, final int to) {
        final int offset = another.addr.isFortran() ? 1 : 0;
        QL.require(from >= offset && to >= from && to <= another.size()+offset, INVALID_ARGUMENTS);
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset();
        final Address.ArrayAddress.ArrayOffset aoff = another.addr.offset(from);
        double sum = 0.0;
        for (int i=0; i<to-from; i++) {
            final double telem = this.$[toff.op()];
            final double aelem = another.$[aoff.op()];
            sum += telem * aelem;
            toff.nextIndex();
            aoff.nextIndex();
        }
        return sum;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 内积
     */
    @Override
    public double innerProduct(final Array another) {
        // 实数时，点积与内积取值相同
        return dotProduct(another);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 内积
     */
    @Override
    public double innerProduct(final Array another, final int from, final int to) {
        // 实数时，点积与内积取值相同
        return dotProduct(another, from, to);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 外积
     */
    @Override
    public Matrix outerProduct(final Array another) {
        final int offset = another.addr.isFortran() ? 1 : 0;
        return outerProduct(another, offset, another.size()+offset);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 外积
     */
    @Override
    public Matrix outerProduct(final Array another, final int from, final int to) {
        final int offset = another.addr.isFortran() ? 1 : 0;
        QL.require(from >= offset && to >= from && to <= another.size()+offset, INVALID_ARGUMENTS);
        final Matrix result = new Matrix(this.size(), to - from);
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset();
        int addr = 0;
        for (int i=0; i<this.size(); i++) {
            final Address.ArrayAddress.ArrayOffset aoff = another.addr.offset(from);
            for (int j=from; j < to; j++) {
                result.$[addr] = this.$[toff.op()] * another.$[aoff.op()];
                addr++;
                aoff.nextIndex();
            }
            toff.nextIndex();
        }
        return result;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 累加
     */
    @Override
    public double accumulate() {
        return accumulate(0, this.size(), 0.0);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 累加
     */
    @Override
    public double accumulate(final double init) {
        return accumulate(0, this.size(), init);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 累加
     */
    @Override
    public double accumulate(final int first, final int last, final double init) {
        QL.require(first>=0 && last>first && last<=size(),  INVALID_ARGUMENTS);
        double sum = init;
        final Address.ArrayAddress.ArrayOffset src = this.addr.offset(first);
        for (int i=0; i<last-first; i++) {
            final double elem = this.$[src.op()];
            sum += elem;
            src.nextIndex();
        }
        return sum;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 相邻差值
     */
    @Override
    public final Array adjacentDifference() {
        final int offset = addr.isFortran() ? 1 : 0;
        return adjacentDifference(offset, size() + offset);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 相邻差值
     */
    @Override
    public final Array adjacentDifference(final int from, final int to) {
        final int offset = addr.isFortran() ? 1 : 0;
        QL.require(from >= offset && to >= from && to <= this.size()+offset, INVALID_ARGUMENTS);
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset(from);
        final Array diff = new Array(to-from, this.flags());
        double prev = this.$[toff.op()];
        toff.nextIndex();
        diff.$[diff.index(offset)] = prev;
        for (int i=1+offset; i<to-from+offset; i++) {
            final double curr = this.$[toff.op()];
            toff.nextIndex();
            diff.$[diff.index(i)] = curr - prev;
            prev = curr;
        }
        return diff;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 相邻差值
     */
    @Override
    public Array adjacentDifference(final Ops.BinaryDoubleOp f) {
        final int offset = addr.isFortran() ? 1 : 0;
        return adjacentDifference(offset, size()+offset, f);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 相邻差值
     */
    @Override
    public Array adjacentDifference(final int from, final int to, final Ops.BinaryDoubleOp f) {
        final int offset = addr.isFortran() ? 1 : 0;
        QL.require(from >= offset && to >= from && to <= this.size()+offset, INVALID_ARGUMENTS);
        final Address.ArrayAddress.ArrayOffset toff = this.addr.offset(from);
        final Array diff = new Array(to-from, this.flags());
        double prev = this.$[toff.op()];
        toff.nextIndex();
        diff.$[diff.index(offset)] = prev;
        for (int i=1+offset; i<to-from+offset; i++) {
            final double curr = this.$[toff.op()];
            toff.nextIndex();
            diff.$[diff.index(i)] = f.op(curr, prev);
            prev = curr;
        }
        return diff;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 转置
     */
    @Override
    public Array transform(final Ops.DoubleOp f) {
        final int offset = addr.isFortran() ? 1 : 0;
        return transform(offset, this.size()+offset, f);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 转置
     */
    @Override
    public Array transform(final int from, final int to, final Ops.DoubleOp f) {
        final int offset = addr.isFortran() ? 1 : 0;
        QL.require(from >= offset && to >= from && to <= this.size()+offset && f!=null, INVALID_ARGUMENTS);
        for (int i=from; i<to; i++) {
            final int idx = this.index(i);
            this.$[idx] = f.op(this.$[idx]);
        }
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 下届
     */
    @Override
    public int lowerBound(final double val) {
        final int offset = addr.isFortran() ? 1 : 0;
        return lowerBound(offset, size()+offset, val, new LessThanPredicate());
    }

    /**
     * @Author  Jiaming Yan
     * @Description 下届
     */
    @Override
    public int lowerBound(final int from, final int to, final double val) {
        return lowerBound(from, to, val, new LessThanPredicate());
    }

    /**
     * @Author  Jiaming Yan
     * @Description 下届
     */
    @Override
    public int lowerBound(final double val, final Ops.BinaryDoublePredicate f) {
        final int offset = addr.isFortran() ? 1 : 0;
        return lowerBound(offset, size()+offset, val, f);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 下届
     */
    @Override
    public int lowerBound(int from, final int to, final double val, final Ops.BinaryDoublePredicate f) {
        final int offset = addr.isFortran() ? 1 : 0;
        QL.require(from>=offset && from<=to && to<=size()+offset, INVALID_ARGUMENTS);
        int len = to - from;
        while (len > 0) {
            final int half = len >> 1;
            final int middle = from-offset + half;
            if (f.op($[addr.op(middle+offset)], val)) {
                from = middle+offset + 1;
                len -= half + 1;
            } else {
                len = half;
            }
        }
        return from;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 上届
     */
    @Override
    public int upperBound(final double val) {
        final int offset = addr.isFortran() ? 1 : 0;
        return upperBound(offset, size()+offset, val);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 上届
     */
    @Override
    public int upperBound(final int from, final int to, final double val) {
        return upperBound(from, to, val, new LessThanPredicate());
    }

    /**
     * @Author  Jiaming Yan
     * @Description 上届
     */
    @Override
    public int upperBound(final double val, final Ops.BinaryDoublePredicate f) {
        final int offset = addr.isFortran() ? 1 : 0;
        return upperBound(offset, size()+offset, val, f);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 上届
     */
    @Override
    public int upperBound(int from, final int to, final double val, final Ops.BinaryDoublePredicate f) {
        final int offset = addr.isFortran() ? 1 : 0;
        QL.require(from>=offset && from<=to && to<=size()+offset, INVALID_ARGUMENTS);
        int len = to - from;
        while (len > 0) {
            final int half = len >> 1;
            final int middle = from-offset + half;
            if (f.op(val, $[addr.op(middle+offset)])) {
                len = half;
            } else {
                from = middle+offset + 1;
                len -= half + 1;
            }
        }
        return from;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 范围
     */
    public Array range(final int col0) {
        return range(col0, cols());
    }

    /**
     * @Author  Jiaming Yan
     * @Description 范围
     */
    public Array range(final int col0, final int col1) {
        final int offset = addr.isFortran() ? 1 : 0;
        QL.require(col0 >= offset && col0 < cols()+offset && col1 >= offset && col1 <= cols()+offset, Address.INVALID_COLUMN_INDEX);
        return new Range(offset, this.addr, $, col0, col1, rows(), cols());
    }

    /**
     * @Author  Jiaming Yan
     * @Description 转换为Fortran
     */
    public Array toFortran() {
        return this.addr.isFortran()
                ?  this
                : new Array(this.rows, this.cols, this.$, this.addr.toFortran());
    }

    /**
     * @Author  Jiaming Yan
     * @Description 转换为Java
     */
    public Array toJava() {
        return this.addr.isFortran()
                ?  new Array(this.rows, this.cols, this.$, this.addr.toJava())
                : this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description
     */
    public Array fill(final double scalar) {
        QL.require(addr.isContiguous(), NON_CONTIGUOUS_DATA);
        final int offset = addr.isFortran() ? 1 : 0;
        Arrays.fill($, begin()-offset, end()-offset, scalar);
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description
     */
    public Array fill(final Array another) {
        QL.require(addr.isContiguous(), NON_CONTIGUOUS_DATA);
        QL.require(another.addr.isContiguous(), NON_CONTIGUOUS_DATA);
        QL.require(this.rows()==another.rows() && this.cols()==another.cols() && this.size()==another.size(), WRONG_BUFFER_LENGTH);
        final int offsetT = this.addr.isFortran() ? 1 : 0;
        final int offsetA = another.addr.isFortran() ? 1 : 0;
        System.arraycopy(another.$, another.begin()-offsetA, this.$, this.begin()-offsetT, another.size());
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 交换
     */
    public Array swap(final Array another) {
        QL.require(addr.isContiguous(), NON_CONTIGUOUS_DATA);
        QL.require(another.addr.isContiguous(), NON_CONTIGUOUS_DATA);
        QL.require(this.rows()==another.rows() && this.cols()==another.cols() && this.size()==another.size(), WRONG_BUFFER_LENGTH);
        final double [] tdata;
        final Address.ArrayAddress taddr;
        tdata = this.$;  this.$ = another.$;  another.$ = tdata;
        taddr = this.addr;  this.addr = another.addr;  another.addr = taddr;
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 排序
     */
    public Array sort() {
        QL.require(addr.isContiguous(), NON_CONTIGUOUS_DATA);
        final int offset = addr.isFortran() ? 1 : 0;
        Arrays.sort($, begin() - offset, end() - offset);
        return this;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 转换为字符
     */
    @Override
    public String toString() {
        final int offset = addr.isFortran() ? 1 : 0;
        final StringBuffer sb = new StringBuffer();
        sb.append("[rows=").append(rows()).append(" cols=").append(cols()).append(" addr=").append(addr).append('\n');
        sb.append("  [ ");
        sb.append(this.$[this.addr.op(offset)]);
        for (int pos = 1+offset; pos < size()+offset; pos++) {
            sb.append(", ");
            sb.append($[addr.op(pos)]);
        }
        sb.append("  ]\n");
        sb.append("]\n");
        return sb.toString();
    }

    /**
     * @Author  Jiaming Yan
     * @Description 迭代器
     */
    @Override
    public Iterator<Double> iterator() {
        return this.addr.offset();
    }

    /**
     * @Author  Jiaming Yan
     * @Description
     */
    private class Range extends Array {

        public Range(
                final int row0,
                final Address.ArrayAddress chain,
                final double[] data,
                final int col0,
                final int col1,
                final int rows, final int cols) {
            super(1,
                    col1 - col0,
                    data,
                    new DirectArrayRowAddress(data, row0, chain, col0, col1, null, true, rows, cols));
        }
    }





}
