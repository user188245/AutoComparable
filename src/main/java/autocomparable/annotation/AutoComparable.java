package autocomparable.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface AutoComparable {

    boolean isLowPriorityFirst() default true;

    boolean generateComparatorGetter() default false;

}
