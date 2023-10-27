package yjm.value.currencies;

import yjm.value.lang.exceptions.LibraryException;
import yjm.value.math.Constants;
import yjm.value.util.Pair;


/**
 * 汇率类
 */
public class ExchangeRate {

    public enum Type {
        Direct,

        Derived
    }

    private Currency source_;

    private Currency target_;

    private double rate_;

    private Type type_;

    /**
     * 一组汇率。如CNY/USD和USD/EUR
     */
    public Pair<ExchangeRate, ExchangeRate> rateChain_;

    public ExchangeRate() {
        this.rate_ = Constants.NULL_REAL;
    }
    
    public ExchangeRate(final ExchangeRate toCopy) {
        //shouldn't matter
        source_ = toCopy.source_;
        target_ = toCopy.target_;
        rate_ = toCopy.rate_;
        type_ = toCopy.type_;
    }
    
    public ExchangeRate(final Currency source,
                        final Currency target,
             final double rate) {
        this.source_ = (source);
        this.target_ = (target);
        this.rate_ = (rate);
        this.type_ = (Type.Direct);
    }

    public Currency source() {
        return source_;
    }

    public Currency target() {
        return target_;
    }

    public Type type() {
        return type_;
    }

    public double rate() {
        return rate_;
    }

    /**
     * 货币转换，根据汇率转换成另一种货币
     */
    public Money exchange(final Money amount) {
        switch (type_) {
            // 直接转换
            case Direct:
                if (amount.currency().eq(source_)) {
                    return new Money(amount.value() * rate_, target_);
                } else if (amount.currency().eq(target_)) {
                    return new Money(amount.value() / rate_, source_);
                } else {
                    throw new LibraryException("汇率无法使用");
                }
            // 两个汇率计算后转换
            case Derived:
                if (amount.currency() == rateChain_.first().source() || amount.currency() == rateChain_.first().target()) {
                    return rateChain_.second().exchange(rateChain_.first().exchange(amount));
                } else if (amount.currency() == rateChain_.second().source() || amount.currency() == rateChain_.second().target()) {
                    return rateChain_.first().exchange(rateChain_.second().exchange(amount));
                } else {
                    throw new LibraryException(" 汇率无法使用");
                }
            default:
                throw new LibraryException("未知的汇率类型");
        }
    }

    /**
     * 生成新的汇率对象及RateChain, CNY/USD, CNY/EUR生成USD/EUR
     */
    public static ExchangeRate chain(final ExchangeRate r1,
                                     final ExchangeRate r2) {
        final ExchangeRate result = new ExchangeRate();
        result.type_ = Type.Derived;
        result.rateChain_ = new Pair<ExchangeRate, ExchangeRate>(new ExchangeRate(r1), new ExchangeRate(r2));
        if (r1.source_.eq(r2.source_)) {
            result.source_ = r1.target_;
            result.target_ = r2.target_;
            result.rate_ = r2.rate_ / r1.rate_;
        } else if (r1.source_.eq(r2.target_)) {
            result.source_ = r1.target_;
            result.target_ = r2.source_;
            result.rate_ = 1.0 / (r1.rate_ * r2.rate_);
        } else if (r1.target_.eq(r2.source_)) {
            result.source_ = r1.source_;
            result.target_ = r2.target_;
            result.rate_ = r1.rate_ * r2.rate_;
        } else if (r1.target_.eq(r2.target_)) {
            result.source_ = r1.source_;
            result.target_ = r2.source_;
            result.rate_ = r1.rate_ / r2.rate_;
        } else {
            throw new LibraryException("汇率无法串联");
        }
        return result;
    }

}
