package auto.autocomparable.annotation;

import java.lang.annotation.*;

/**
 * {@code AutoComparableTarget} must be located at field variable or method enclosed with the class annotated with {@link AutoComparable}
 * <p>
 * If the {@code AutoComparableTarget} variable is field variable, then the type of variable must be sortable.
 * <p>
 * If the {@code AutoComparableTarget} variable is method, then it must not have any of parameters, and the return type of method also must be sortable.
 * <p>
 * The "sortable" type must satisfy one condition at least among below 3 conditions.
 * <p>
 * 1. the type is Comparable or the sub-interface of Comparable
 * <p>
 * 2. the type is primitive (e.g. int, long, boolean, float)
 * <p>
 * 3. the type is concrete class annotated with {@link AutoComparable}
 * <p>
 * {@code AutoComparableTarget} variable must define a {@code priority} which used as an order of priority.
 * <p>
 * {@code AutoComparableTarget} variable define a {@code order} as a the way of order.
 * <p>
 * Even though the {@code AutoComparableTarget} variable is not a sortable type, it can sortable by inputting a specific method at {@code alternativeCompareMethod} explicitly
 * that contains int return type and only 2 parameters with same type with {@code AutoComparableTarget} variable. if the method is external, then it also must be static method. generic static method is not allowed in case.
 *
 * @author user188245
 * @see AutoComparable
 *
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface AutoComparableTarget{

    /**
     * Determine an order of priority. the lowest value is sorted first if isLowPriorityFirst on {@link AutoComparable} is true.
     * @return the value of priority
     */
    int priority();

    /**
     * Determine a way of sort.
     * @return ascending order if Order.ASC, otherwise reverse order.
     */
    Order order() default Order.ASC;

    /**
     * Use an alternative method instead. it must be accessible.
     * @return a canonical name of method. (i.e. "Integer.compare")
     */
    String alternativeCompareMethod() default "";

}