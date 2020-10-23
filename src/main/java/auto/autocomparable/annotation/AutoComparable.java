package auto.autocomparable.annotation;

import java.lang.annotation.*;

/**
 * {@code @AutoComparable} make an annotated class automatically implemented
 * Comparable interface containing self class as a the generic argument
 * through the {@link auto.autocomparable.annotation.processing.AutoComparableProcessor}
 * <p>
 * The class annotated with {@code @AutoComparable} must contain a field variable or method annotated with {@link AutoComparableTarget}.
 * <p>
 * The method "compareTo" defined by Comparable is determined by a properties included on arguments of {@link AutoComparableTarget}.
 * <p>
 * All of classes annotated with {@code @AutoComparable} must not implement Comparable interface directly as well as "compareTo" method, unless the types of parameter are different (method-overloading),
 * otherwise {@link auto.util.AnnotationProcessingException} will be thrown.
 * <p>
 * {@code @AutoComparable} is only supported on "class". not supported on "interface" or "enum"
 * <p>
 * The Sorting order of {@code @AutoComparable} is determined by the argument {@code "isLowPriorityFirst"}.
 * if the argument is true, then lowest priority of {@link AutoComparableTarget} will be sorted first. (otherwise highest priority first).
 *
 * @author user188245
 * @see AutoComparableTarget
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface AutoComparable {

    /**
     * Determine what {@link AutoComparableTarget} variable should be sorted first using a priority value.
     * @return true if the lowest priority target is sorted first, otherwise false.
     */
    boolean isLowPriorityFirst() default true;

//    boolean generateComparatorGetter() default false;

}
