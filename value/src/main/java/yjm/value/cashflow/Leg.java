package yjm.value.cashflow;

import yjm.value.QL;

import java.util.ArrayList;

/**
 * @author Jiaming Yan
 * @description: Leg类
 */
public class Leg extends ArrayList<Cashflow> implements Cloneable {

    /**
     * @author Jiaming Yan
     * @description: 构造方法，创建一个空的ArrayList<Cashflow>
     */
    public Leg() {
        super();
    }

    /**
     * @author Jiaming Yan
     * @description: 构造方法，创建一个n维的ArrayList<Cashflow>
     */
    public Leg(final int n) {
        super(n);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回leg的第一个Cashflow
     */
    public Cashflow first() {
        QL.require(this.size() > 0 , "无现金流");
        return this.get(0);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回leg的最后一个Cashflow
     */
    public Cashflow last() {
        QL.require(this.size() > 0 , "无现金流");
        return this.get(this.size()-1);
    }

    /**
     * @Author  Jiaming Yan
     * @Description 复制Leg对象
     */
    public Object clone() {
        return (Leg)super.clone();
    }


}
