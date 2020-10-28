package auto.autocomparable.annotation;

import java.lang.annotation.*;

/**
 * {@code @AutoComparable} makes an annotated class automatically implemented
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
 * The Sorting order of {@code @AutoComparable} is determined by the argument {@link AutoComparable#isLowPriorityFirst}.
 * if the argument is true, then lowest {@link AutoComparableTarget#priority} of {@link AutoComparableTarget} will be sorted first. (otherwise highest priority first).
 * <p>
 * The Sample Code
 * <blockquote><pre>
 * import auto.autocomparable.annotation.AutoComparable;
 * import auto.autocomparable.annotation.AutoComparableTarget;
 * import auto.autocomparable.annotation.Order;
 *
 * {@code @AutoComparable}
 * public class Boo {
 *
 *     {@code @AutoComparableTarget(priority = 1)}
 *     int a;
 *
 *     {@code @AutoComparableTarget(priority = 2, order = Order.DESC)}
 *     int b;
 *
 *     public Boo(int a, int b){
 *         this.a = a;
 *         this.b = b;
 *     }
 *
 *     {@code @Override}
 *     public String toString() {
 *         return "(" + a + "," + b + ")";
 *     }
 * }
 * </pre></blockquote>
 * <p>
 * The Sample Code 2
 * <blockquote><pre>
 *     ...
 * ArrayList<Boo> booList = new ArrayList<Boo>();
 * booList.add(new Boo(3,3));
 * booList.add(new Boo(1,3));
 * booList.add(new Boo(2,2));
 * booList.add(new Boo(4,1));
 * booList.add(new Boo(-1,6));
 * booList.add(new Boo(3,0));
 * System.out.print("Before Sort : ");
 * System.out.println(booList);
 * Collections.sort(booList);
 * System.out.print("After  Sort : ");
 * System.out.println(booList);
 *     ...
 *
 * </pre></blockquote>
 * <p>
 * Expected Result of Code 2
 * <blockquote><pre>
 * Before Sort : [(3,3), (1,3), (2,2), (4,1), (-1,6), (3,0)]
 * After  Sort : [(-1,6), (1,3), (2,2), (3,3), (3,0), (4,1)]
 * </pre></blockquote>
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
     *
     * @return true if the lowest priority target is sorted first, otherwise false.
     */
    boolean isLowPriorityFirst() default true;

//    boolean generateComparatorGetter() default false;

}
