package yjm.value.lang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;


@Target({ ElementType.TYPE_USE, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.PARAMETER })
public @interface Natural {
    // tagging annotation
}
