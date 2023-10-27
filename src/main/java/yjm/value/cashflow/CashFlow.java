package yjm.value.cashflow;

import yjm.value.time.Date;
import yjm.value.util.PolymorphicVisitor;
import yjm.value.util.Visitor;

/**
 * @Author Jiaming Yan
 * @Description 现金流抽象类
 */

public abstract class CashFlow extends Event implements Comparable<CashFlow> {

    /**
     * @Author Jiaming Yan
     * @Description 抽象方法，返回现金流大小
     */
    public abstract double amount();

    /**
     * @Author Jiaming Yan
     * @Description 抽象方法，返回现金流的日期
     */
    public abstract Date date();


    /**
     * @Author Jiaming Yan
     * @Description 比较现金流
     */
    public int compareTo(final CashFlow c2) {
        //先比较日期，日期在前返回-1
        if (date().lt(c2.date())) {
            return -1;
        }
        //日期相同，比较金额，小于返回-1，大于返回0
        if (date().equals(c2.date())) {
            try {
                if (amount() < c2.amount()) {
                    return -1;
                }
            } catch (final Exception e) {
                return -1;
            }
            return 0;
        }

        return 1;
    }

    @Override
    public void accept(final PolymorphicVisitor pv) {
        final Visitor<CashFlow> v = (pv != null) ? pv.visitor(this.getClass()) : null;
        if (v != null) {
            v.visit(this);
        } else {
            super.accept(pv);
        }
    }

}
