package yjm.value.lang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;


@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
public @interface PackagePrivate {
    // tagging annotation
}
