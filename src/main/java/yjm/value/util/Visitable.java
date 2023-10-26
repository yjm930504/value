package yjm.value.util;

/**
 * @Author  Jiaming Yan
 * @Description 定义Visitable
 */
public interface Visitable<T> {

    /**
     * @Author  Jiaming Yan
     * @Description 确定Visitor是否有资格处理数据，若有资格accept负责将此数据传递给访问者
     */
    public void accept(Visitor<T> v);

}
