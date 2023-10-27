package yjm.value.currencies;

import yjm.value.math.Rounding;


public class Currency implements Cloneable {

    protected Data data;

    public Currency() {
    }

    public final String name() {
        return data.name;
    }

    public final String code() {
        return data.code;
    }

    public final int numericCode() {
        return data.numeric;
    }

    public final String symbol() {
        return data.symbol;
    }

    public final String fractionSymbol() {
        return data.fractionSymbol;
    }

    public final int fractionsPerUnit() {
        return data.fractionsPerUnit;
    }

    public final Rounding rounding() {
        return data.rounding;
    }

    public final String format() {
        return data.formatString;
    }

    public final boolean empty() {
        return data == null;
    }

    public final Currency triangulationCurrency() {
        return data.triangulated;
    }

    public final boolean eq(final Currency currency) {
        return equals(currency);
    }

    public final boolean ne(final Currency currency) {
        return !(eq(currency));
    }

    public static final boolean operatorEquals(final Currency c1, final Currency c2) {
        return c1.equals(c2);
    }

    public static final boolean operatorNotEquals(final Currency c1, final Currency c2) {
        // eating our own dogfood
        return !(Currency.operatorEquals(c1, c2));
    }

    @Override
    public String toString() {
        if (!empty())
            return code();
        else
            return "空货币";
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        return obj instanceof Currency &&
                ((Currency) obj).fEquals(this);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((data == null) ? 0 : name().hashCode());
        return result;
    }

    protected boolean fEquals(Currency other) {
        if (this.empty() && other.empty())
            return true;
        if (this.name().equals(other.name()))
            return true;
        return false;
    }

    @Override
    protected Currency clone() {
        final Currency currency = new Currency();
        if (data != null) {
            currency.data = data.clone();
        }
        return currency;

    }

    protected static class Data implements Cloneable {

        /**
         * 货币名, e.g, "U.S. Dollar"
         */
        private final String name;

        /**
         * ISO三字代码, e.g, "USD"
         */
        private final String code;

        /**
         * ISO数字代码, e.g, "840"
         */
        private final int numeric;

        /**
         * 符号, e.g, "$"
         */
        private final String symbol;

        /**
         * 分数符号, e.g, "p"
         */
        private final String fractionSymbol;

        /**
         * 单位中小数部分数量, e.g, 100
         */
        private final int fractionsPerUnit;

        /**
         * 近似规则
         */
        private final Rounding rounding;

        /**
         * value, code and symbol
         */
        private final String formatString;

        /**
         * 三角兑换货币
         */
        private final Currency triangulated;

        public Data(final String name, final String code, final int numericCode, final String symbol, final String fractionSymbol,
                    final int fractionsPerUnit, final Rounding rounding, final String formatString) {
            this(name, code, numericCode, symbol, fractionSymbol, fractionsPerUnit, rounding, formatString, new Currency());
        }

        public Data(final String name, final String code, final int numericCode, final String symbol, final String fractionSymbol,
                    final int fractionsPerUnit, final Rounding rounding, final String formatString, final Currency triangulationCurrency) {
            this.name = (name);
            this.code = (code);
            this.numeric = (numericCode);
            this.symbol = (symbol);
            this.fractionSymbol = (fractionSymbol);
            this.fractionsPerUnit = (fractionsPerUnit);
            this.rounding = (rounding);
            this.triangulated = (triangulationCurrency);
            this.formatString = (formatString);
        }

        @Override
        public Data clone() {
            final Data data = new Data(name, code, numeric, symbol, fractionSymbol, fractionsPerUnit, rounding, formatString,
                    triangulated.clone());
            return data;
        }

    }
}
