package yjm.value.math;

import yjm.value.Main;
import yjm.value.lang.exceptions.LibraryException;

/**
 * 近似规则
 */
public class Rounding {

    /**
     * 保留几位小数
     */
    private int precision_;
    public Type type_;

    /**
     * 默认5，四舍五入
     */
    private int digit_;

    /**
     * 近似类型
     */
    public enum Type {

        None,

        Up,

        Down,

        Closest,

        Floor,

        Ceiling
    }


    public Rounding() {
        this.type_ = Type.None;
    }

    /**
     * 默认5，四舍五入
     */
    public Rounding(final int precision) {
        this(precision, Type.Closest, 5);
    }

    public Rounding(final int precision, final Type type, final int digit) {
        this.precision_ = (precision);
        this.type_ = (type);
        this.digit_ = (digit);
    }

    public int precision() {
        return precision_;
    }

    public Type type() {
        return type_;
    }

    public int roundingDigit() {
        return digit_;
    }


    /**
     * 根据近似规则，返回近似后的数值
     */
    public final double operator(final double value) {

        if (type_ == Type.None)
            return value;

        final double mult = Math.pow(10.0, precision_);
        final boolean neg = (value < 0.0);
        double lvalue = Math.abs(value) * mult;
        final double integral = ((int) lvalue);
        final double modVal = (lvalue - (int) lvalue); //精度后的小数点
        lvalue -= modVal;

        switch (type_) {
            case Down:
                break;
            case Up:
                lvalue += 1.0;
                break;
            case Closest:
                if (modVal >= (digit_ / 10.0)) {
                    lvalue += 1.0;
                }
                break;
            case Floor:
                if (!neg) {
                    if (modVal >= (digit_ / 10.0)) {
                        lvalue += 1.0;
                    }
                }
                break;
            case Ceiling:
                if (neg) {
                    if (modVal >= (digit_ / 10.0)) {
                        lvalue += 1.0;
                    }
                }
                break;
            default:
                throw new LibraryException("不支持的近似规则");
        }
        return (neg) ? -(lvalue / mult) : lvalue / mult;
    }

    /**
     * 向上近似
     */
    public static class UpRounding extends Rounding {
        public UpRounding(final int precision) {
            this(precision, 5);
        }
        public UpRounding(final int precision, final int digit) {
            super(precision, Type.Up, digit);
        }
    }

    /**
     * 向下近似
     */
    public static class DownRounding extends Rounding {
        public DownRounding(final int precision) {
            this(precision, 5);
        }
        public DownRounding(final int precision, final int digit) {
            super(precision, Type.Down, digit);
        }
    }


    /**
     * Closest
     */
    public static class ClosestRounding extends Rounding {
        public ClosestRounding(final int precision) {
            this(precision, 5);
        }
        public ClosestRounding(final int precision, final int digit) {
            super(precision, Type.Closest, digit);
        }
    }

    /**
     * Ceiling
     */
    public static class CeilingTruncation extends Rounding {
        public CeilingTruncation(final int precision) {
            this(precision, 5);
        }
        public CeilingTruncation(final int precision, final int digit) {
            super(precision, Type.Ceiling, digit);
        }
    }

    /**
     * Floor
     */
    public static class FloorTruncation extends Rounding {
        public FloorTruncation(final int precision) {
            this(precision, 5);
        }
        public FloorTruncation(final int precision, final int digit) {
            super(precision, Type.Floor, digit);
        }
    }

}

