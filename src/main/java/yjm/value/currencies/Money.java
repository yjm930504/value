package yjm.value.currencies;

import yjm.value.QL;
import yjm.value.lang.exceptions.LibraryException;
import yjm.value.math.Closeness;

/**
 * 现金类
 */
public class Money implements Cloneable {

    public static ConversionType conversionType;
    public static Currency baseCurrency;
    private double value_;
    private Currency currency_;

    public Money() {
        QL.validateExperimentalMode();
        this.value_ = (0.0);
    }

    public Money(final Currency currency, final double value) {
        QL.validateExperimentalMode();
        this.value_ = (value);
        this.currency_ = (currency);
    }

    public Money(final double value, final Currency currency) {
        QL.validateExperimentalMode();
        this.value_ = (value);
        this.currency_ = (currency.clone());
    }

    public static Money multiple(final Currency c, final double value) {
        return new Money(value, c);
    }

    public static Money multiple(final double value, final Currency c) {
        return new Money(value, c);
    }

    @Override
    public Money clone() {
        final Money money = new Money();
        money.currency_ = currency_.clone();
        money.value_ = value_;
        return money;
    }

    public Currency currency() {
        return currency_;
    }

    public double value() {
        return value_;
    }

    public Money rounded() {
        return new Money(currency_.rounding().operator(value_), currency_);
    }

    public Money mulAssign(final double x) {
        value_ *= x;
        return this;
    }

    public Money divAssign(final double x) {
        value_ /= x;
        return this;
    }

    public Money add(final Money money) {
        final Money tmp = clone();
        tmp.addAssign(money);
        return tmp;
    }

    public Money sub(final Money money) {
        final Money tmp = clone();
        tmp.subAssign(money);
        return tmp;
    }

    public Money mul(final double x) {
        final Money tmp = clone();
        tmp.mulAssign(x);
        return tmp;
    }

    public Money div(final double x) {
        final Money tmp = clone();
        tmp.value_ /= x;
        return tmp;
    }

    public boolean notEquals(final Money money) {
        return !(this.equals(money));
    }

    public boolean greater(final Money money) {
        return money.greater(this);

    }

    public boolean greaterEqual(final Money money) {
        return money.greaterEqual(this);
    }

    /**
     * 币种转换
     */
    public void convertTo(final Currency target) {
        if (currency().ne(target)) {
            final ExchangeRate rate = ExchangeRateManager.getInstance().lookup(currency(), target);
            final Money money = rate.exchange(this).rounded();
            this.currency_ = money.currency_;
            this.value_ = money.value_;
        }
    }


    /**
     * 转换为基础货币
     */
    public void convertToBase() {
        QL.require((!baseCurrency.empty()), "未设置基础货币");
        convertTo(baseCurrency);
    }


    /**
     * 加法运算
     */
    public Money addAssign(final Money money) {

        // 币种相同直接相加
        if (this.currency_.eq(money.currency_)) {
            this.value_ += money.value_;

            //转换为基础货币币种
        } else if (conversionType == ConversionType.BaseCurrencyConversion) {
            this.convertToBase();
            final Money tmp = money.clone();
            tmp.convertToBase();
            this.addAssign(tmp);

            //转换为此币种
        } else if (conversionType == ConversionType.AutomatedConversion) {
            final Money tmp = money.clone();
            tmp.convertTo(currency_);
            this.addAssign(tmp);
        } else
            throw new LibraryException("币种不匹配且无币种转换方式");
        return this;
    }

    /**
     * 减法运算
     */
    public Money subAssign(final Money money) {
        if (currency_.eq(money.currency_)) {
            value_ -= money.value_;
        } else if (conversionType == ConversionType.BaseCurrencyConversion) {
            this.convertToBase();
            final Money tmp = money.clone();
            tmp.convertToBase();
            this.subAssign(tmp);
        } else if (conversionType == ConversionType.AutomatedConversion) {
            final Money tmp = money.clone();
            tmp.convertTo(currency_);
            this.subAssign(tmp);
        } else
            throw new LibraryException("币种不匹配且无币种转换方式");
        return this;
    }

    /**
     * 除法运算
     */
    public double div(final Money money) {
        if (currency().eq(money.currency()))
            return value_ / money.value();
        else if (conversionType == ConversionType.BaseCurrencyConversion) {
            final Money tmp1 = this.clone();
            tmp1.convertToBase();
            final Money tmp2 = money.clone();
            tmp2.convertToBase();
            return this.div(tmp2);
        } else if (conversionType == ConversionType.AutomatedConversion) {
            final Money tmp = money.clone();
            tmp.convertTo(money.currency());
            return this.div(tmp);
        } else
            throw new LibraryException("币种不匹配且无币种转换方式");
    }

    /**
     * 判断是否相等
     */
    public boolean equals(final Money money) {
        if (currency().eq(money.currency()))
            return value() == money.value();
        else if (conversionType == ConversionType.BaseCurrencyConversion) {
            final Money tmp1 = this.clone();
            tmp1.convertToBase();
            final Money tmp2 = money.clone();
            tmp2.convertToBase();
            return tmp1.equals(tmp2);
        } else if (conversionType == ConversionType.AutomatedConversion) {
            final Money tmp = money.clone();
            tmp.convertTo(this.currency());
            return this.equals(tmp);
        } else
            throw new LibraryException(" 币种不匹配且无币种转换方式");
    }

    /**
     * 判断是否小于
     */
    public boolean less(final Money money) {
        if (this.currency().eq(money.currency()))
            return value() < money.value();
        else if (conversionType == ConversionType.BaseCurrencyConversion) {
            final Money tmp1 = this.clone();
            tmp1.convertToBase();
            final Money tmp2 = money;
            tmp2.convertToBase();
            return tmp1.less(tmp2);
        } else if (conversionType == ConversionType.AutomatedConversion) {
            final Money tmp = money;
            tmp.convertTo(currency());
            return this.less(tmp);
        } else
            throw new LibraryException(" 币种不匹配且无币种转换方式");
    }

    /**
     * 判断是否小于等于
     */
    public boolean lessEquals(final Money money) {
        if (currency().eq(money.currency()))
            return value() <= money.value();
        else if (conversionType == ConversionType.BaseCurrencyConversion) {
            final Money tmp1 = this.clone();
            tmp1.convertToBase();
            final Money tmp2 = money;
            tmp2.convertToBase();
            return tmp1.less(tmp2);
        } else if (conversionType == ConversionType.AutomatedConversion) {
            final Money tmp = money.clone();
            ;
            tmp.convertTo(this.currency());
            return this.less(tmp);
        } else
            throw new LibraryException(" 币种不匹配且无币种转换方式");
    }

    /**
     * 判断是否接近
     */
    public boolean close(final Money money, final int n) {
        if (currency().eq(money.currency()))
            return Closeness.isClose(value(), money.value(), n);
        else if (conversionType == ConversionType.BaseCurrencyConversion) {
            final Money tmp1 = this.clone();
            tmp1.convertToBase();
            final Money tmp2 = money.clone();
            tmp2.convertToBase();
            return tmp1.close(tmp2, n);
        } else if (conversionType == ConversionType.AutomatedConversion) {
            final Money tmp = money.clone();
            tmp.convertTo(this.currency());
            return this.close(tmp, n);
        } else
            throw new LibraryException(" 币种不匹配且无币种转换方式");
    }

    /**
     * 判断是否接近，条件较close宽松
     */
    public boolean close_enough(final Money money, final int n) {
        if (currency().eq(money.currency()))
            return Closeness.isCloseEnough(value(), money.value(), n);
        else if (conversionType == ConversionType.BaseCurrencyConversion) {
            final Money tmp1 = this.clone();
            tmp1.convertToBase();
            final Money tmp2 = money;
            tmp2.convertToBase();
            return tmp1.close_enough(tmp2, n);
        } else if (conversionType == ConversionType.AutomatedConversion) {
            final Money tmp = money;
            tmp.convertTo(currency());
            return this.close_enough(tmp, n);
        } else
            throw new LibraryException(" 币种不匹配且无币种转换方式");
    }

    @Override
    public String toString() {
        final Currency currency = currency();
        return String.format(currency.format(), rounded().value_, currency.code(), currency.symbol());
    }


    /**
     * 转换类型枚举类
     */
    public enum ConversionType {

        /**
         * 不转换
         */
        NoConversion,

        /**
         * 转换为基础货币
         */
        BaseCurrencyConversion,

        /**
         * 转换为第一个货币类型
         */
        AutomatedConversion
    }

}
