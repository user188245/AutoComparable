package autocomparable.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface AutoComparableTarget{

    int priority() default 0;

    Order order() default Order.ASC;

}