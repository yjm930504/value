package yjm.value.util;


/**
 * @Author  Jiaming Yan
 * @Description 定义Visitor
 */
public interface Visitor<T> {

    /**
     * @Author  Jiaming Yan
     * @Description 处理数据
     */
    public void visit(T element);
}
