package yjm.value.util;

public interface PolymorphicVisitor {

    public <T> Visitor<T> visitor(Class<? extends T> element);

}
