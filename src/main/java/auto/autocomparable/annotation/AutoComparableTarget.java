package auto.autocomparable.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface AutoComparableTarget{

    int priority();

    Order order() default Order.ASC;

    String alternativeCompareMethod() default "";

}