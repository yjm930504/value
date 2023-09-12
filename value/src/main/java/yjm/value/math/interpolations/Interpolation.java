package yjm.value.math.interpolations;

import yjm.value.math.Ops;
import yjm.value.math.matrixutilities.Array;


/**
 * @Author  Jiaming Yan
 * @Description 一维的插值接口
 */
public interface Interpolation extends Extrapolator, Ops.DoubleOp{

    public boolean empty();
    public double op(final double x, boolean allowExtrapolation);
    public double primitive(final double x, boolean allowExtrapolation);
    public double derivative(final double x, boolean allowExtrapolation);
    public double secondDerivative(final double x, boolean allowExtrapolation) ;
    public double op(final double x);
    public double primitive(final double x);
    public double derivative(final double x);
    public double secondDerivative(final double x);
    public double xMin();
    public double xMax();
    public boolean isInRange(final double x);
    public void update();

    public interface Interpolator {
        public boolean global();
        public int requiredPoints() ;
        public Interpolation interpolate(final Array vx, final Array vy);

    }

}
