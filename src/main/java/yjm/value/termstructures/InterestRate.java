package yjm.value.termstructures;

import yjm.value.daycounters.DayCounter;
import yjm.value.lang.exceptions.LibraryException;
import yjm.value.time.Date;
import yjm.value.time.Frequency;
import yjm.value.QL;

public class InterestRate {

    private final double rate;
    private DayCounter dc;
    private Compounding compound;
    private boolean freqMakesSense;
    //复利频率，以数字表示，365代表每日
    private int freq;

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数，默认利率为0
     */
    public InterestRate() {
        this.rate = 0.0;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数，默认复利方式为连续复利
     */
    public InterestRate(final double r, final DayCounter dc) {
        this(r, dc, Compounding.Continuous);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数，默认计息频率为每年
     */
    public InterestRate(final double r, final DayCounter dc, final Compounding comp) {
        this(r, dc, comp, Frequency.Annual);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 构造函数
     */
    public InterestRate(final double r, final DayCounter dc, final Compounding comp, final Frequency freq) {

        this.rate = r;
        this.dc = dc;
        this.compound = comp;
        this.freqMakesSense = false;

        if (this.compound == Compounding.Compounded || this.compound == Compounding.SimpleThenCompounded) {
            freqMakesSense = true;
            QL.require(freq != Frequency.Once && freq != Frequency.NoFrequency,
                    "频率" + freq.toString() + "与复利方式" + comp.toString() + "不匹配");
            this.freq = freq.toInteger();
        }
    }

    /**
     * @Author  Jiaming Yan
     * @Description 根据复利方式，计算复利因子
     */
    public final double compoundFactor(final double time) {

        final double t = time;
        QL.require(t >= 0.0 , "计算复利因子时年化时间不可为空");
        QL.require(!Double.isNaN(rate) , "计算复利因子时利率不可为空");
        final double r = rate;
        if (compound == Compounding.Simple) {
            return 1.0 + r * t;
        } else if (compound == Compounding.Compounded) {
            return Math.pow((1 + r / freq), (freq * t));
        } else if (compound == Compounding.Continuous) {
            return Math.exp((r * t));
        } else if (compound == Compounding.SimpleThenCompounded) {
            if (t < (1 / (double) freq)) {
                return 1.0 + r * t;
            } else {
                return Math.pow((1 + r / freq), (freq * t));
            }
        } else {
            throw new LibraryException("不支持的复利方式");
        }
    }

    /**
     * @Author  Jiaming Yan
     * @Description 传入计息区间，计算复利因子
     */
    public final double compoundFactor(final Date d1, final Date d2) {
        return compoundFactor(d1, d2, new Date(), new Date());
    }

    /**
     * @Author  Jiaming Yan
     * @Description 传入计息区间，计算复利因子
     */
    public final double compoundFactor(final Date d1, final Date d2,
                                       final Date refStart, final Date refEnd) {
        double t = dc.yearFraction(d1, d2, refStart, refEnd);
        return compoundFactor(t);
    }

    public final DayCounter dayCounter() {
        return this.dc;
    }

    public final Compounding compounding() {
        return this.compound;
    }

    public final Frequency frequency() {
        return freqMakesSense ? Frequency.valueOf(this.freq) : Frequency.NoFrequency;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 传入年化时间，计算折现因子
     */
    public final double discountFactor(final double t) {
        final double factor = compoundFactor(t);
        return 1.0d / factor;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 传入计息区间，计算折现因子
     */
    public final double discountFactor(final Date d1, final Date d2) {
        return discountFactor(d1, d2, new Date());
    }

    /**
     * @Author  Jiaming Yan
     * @Description 传入计息区间，计算折现因子
     */
    public final double discountFactor(final Date d1, final Date d2, final Date refStart) {
        return discountFactor(d1, d2, refStart, new Date());
    }

    /**
     * @Author  Jiaming Yan
     * @Description 传入计息区间，计算折现因子
     */
    public final double discountFactor(final Date d1, final Date d2, final Date refStart, final Date refEnd) {
        final double t = this.dc.yearFraction(d1, d2, refStart, refEnd);
        return discountFactor(t);
    }


    public final InterestRate equivalentRate(final double t, final Compounding comp) {
        return equivalentRate(t, comp, Frequency.Annual);
    }

    public final InterestRate equivalentRate(final/* @Time */double t, final Compounding comp, final Frequency freq) {
        return impliedRate(compoundFactor(t), t, this.dc, comp, freq);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回不同日算惯例和计息频率的等价利率对象
     */
    public final InterestRate equivalentRate(
            final Date d1,
            final Date d2,
            final DayCounter resultDC,
            final Compounding comp,
            final Frequency freq) {
        QL.require(d1.lt(d2) , "开始日期不可晚于结束日期");

        //根据日算惯例dc计算年化时间
        final double t1 = this.dc.yearFraction(d1, d2);

        //根据日算惯例dresultDC计算年化时间
        final double t2 = resultDC.yearFraction(d1, d2);

        return impliedRate(compoundFactor(t1), t2, resultDC, comp, freq);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 传入复利因子，返回不同日算惯例和计息频率的利率对象
     */
    static public InterestRate impliedRate(final double c, final double time,
                                           final DayCounter resultDC, final Compounding comp, final Frequency freq) {

        final double t = time;
        final double f = freq.toInteger();
        QL.require(c > 0.0 , "复利因子必须为正数");
        QL.require(t > 0.0 , "年化时间必须为正数");

        double rate;

        switch (comp) {
            case Simple:
                rate = (c - 1) / t;
                break;
            case Compounded:
                rate = (Math.pow(c, (1 / (f * t))) - 1) * f;
                break;
            case Continuous:
                rate = Math.log(c) / t;
                break;
            case SimpleThenCompounded:
                if (t <= (1 / f)) {
                    rate = (c - 1) / t;
                } else {
                    rate = (Math.pow(c, (1 / (f * t))) - 1) * f;
                }
                break;
            default:
                throw new LibraryException("不支持的复利方式");
        }

        return new InterestRate(rate, resultDC, comp, freq);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 传入复利因子，返回不同日算惯例和计息频率的利率对象
     */
    public static InterestRate impliedRate(final double compound, final double t,
                                           final DayCounter resultDC, final Compounding comp) {
        return impliedRate(compound, t, resultDC, comp, Frequency.Annual);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 传入复利因子，返回不同日算惯例和计息频率的利率对象
     */
    public static InterestRate impliedRate(final double compound, final Date d1, final Date d2,
                                           final DayCounter resultDC, final Compounding comp) {
        return impliedRate(compound, d1, d2, resultDC, comp, Frequency.Annual);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 传入复利因子，返回不同日算惯例和计息频率的利率对象
     */
    public static InterestRate impliedRate(final double compound, final Date d1, final Date d2,
                                           final DayCounter resultDC, final Compounding comp, final Frequency freq) {
        QL.require(d1.le(d2) , "计算隐含利率时，起始日期应该不晚于结束日期");
        final double t = resultDC.yearFraction(d1, d2);
        return impliedRate(compound, t, resultDC, comp, freq);
    }


    /**
     * @Author  Jiaming Yan
     * @Description 传入复利因子，返回不同日算惯例和计息频率的利率对象
     */
    @Override
    public String toString() {
        if (rate == 0.0) {
            return "利率为0";
        }

        final StringBuilder sb = new StringBuilder();
        sb.append(rate).append(' ').append(dc).append(' ');
        if (compound == Compounding.Simple) {
            sb.append("简单复利");
        } else if (compound == Compounding.Compounded) {
            if ((freq == Frequency.NoFrequency.toInteger()) || (freq == Frequency.Once.toInteger())) {
                throw new IllegalArgumentException( "利率的复利方式不支持此频率:" + Frequency.valueOf(freq) );
            } else {
                sb.append("以" + Frequency.valueOf(freq) + "为频率复利");
            }
        } else if (compound == Compounding.Continuous) {
            sb.append("连续复利");
        } else if (compound == Compounding.SimpleThenCompounded) {
            if ((freq == Frequency.NoFrequency.toInteger()) || (freq == Frequency.Once.toInteger())) {
                throw new IllegalArgumentException("利率的复利方式不支持此频率:" + Frequency.valueOf(freq) );
            } else {
                sb.append("先简单复利" + (12 / freq) + "月, 再以" + Frequency.valueOf(freq) + "复利");
            }
        } else {
            throw new LibraryException("未知的复利方式");
        }
        return sb.toString();
    }

    public final double rate() {
        return rate;
    }

}
