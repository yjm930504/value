package org.yjm.math.matrixutilities;

import org.yjm.math.Ops;
import org.yjm.math.Ops.DoubleOp;


/**
 * @Author  Jiaming Yan
 * @Description 代数计算接口
 */
public interface Algebra<T> {
    public T addAssign(final double scalar);
    public T addAssign(final T another);
    public T subAssign(final double scalar);
    public T subAssign(final T another);
    public T mulAssign(final double scalar);
    public T mulAssign(final T another);
    public T divAssign(final double scalar);
    public T divAssign(final T another);
    public T add(final double scalar);
    public T add(final T another);
    public T sub(final double scalar);
    public T sub(final T another);
    public T mul(final double scalar);
    public T mul(final T another);
    public T div(final double scalar);
    public T div(final T another);
    public T negative();
    public T mul(final Matrix matrix);
    public abstract double dotProduct(final T another);
    public abstract double dotProduct(final T another, final int from, final int to);
    public abstract double innerProduct(final T another);
    public abstract double innerProduct(final T another, final int from, final int to);
    public abstract Matrix outerProduct(final T another);
    public abstract Matrix outerProduct(final T another, final int from, final int to);
    public T adjacentDifference();
    public T adjacentDifference(final int from, final int to);
    public T adjacentDifference(final Ops.BinaryDoubleOp f);
    public T adjacentDifference(final int from, final int to, final Ops.BinaryDoubleOp f);
    public double min();
    public double min(final int from, final int to);
    public double max();
    public double max(final int from, final int to);
    public T abs();
    public T sqr();
    public T sqrt();
    public T log();
    public T exp();
    public double accumulate();
    public double accumulate(final double init);
    public double accumulate(final int from, final int to, final double init);
    public T transform(final DoubleOp func);
    public T transform(final int first, final int last, final Ops.DoubleOp f);
    public abstract int lowerBound(final double val);
    public abstract int lowerBound(int from, final int to, final double val);
    public abstract int lowerBound(final double val, final Ops.BinaryDoublePredicate f);
    public abstract int lowerBound(int from, final int to, final double val, final Ops.BinaryDoublePredicate f);
    public abstract int upperBound(final double val);
    public abstract int upperBound(int first, final int last, final double val);
    public abstract int upperBound(final double val, final Ops.BinaryDoublePredicate f);
    public abstract int upperBound(int first, final int last, final double val, final Ops.BinaryDoublePredicate f);

}
