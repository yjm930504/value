package org.yjm.math;

public interface Ops {

    public interface Op<A,R> {
        public R op(A a);
    }

    public interface DoubleOp {
        public double op(double x);
    }

    public interface BinaryDoubleOp {
        public double op(double x, double y);
    }

    public interface IntToDouble {
        public double op(int x);
    }

    public interface ObjectToDouble<A> {
        public double op(A a);
    }

    public static interface DoublePredicate {
        public boolean op(double a);
    }

    public static interface BinaryDoublePredicate {
        public boolean op(double a, double b);
    }

    public static interface DoubleGenerator {
        public double op();
    }

}
