package autocomparable.mock;

import autocomparable.annotation.AutoComparable;
import autocomparable.annotation.AutoComparableTarget;
import autocomparable.annotation.Order;
import jdk.internal.util.ArraysSupport;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@AutoComparable(isLowPriorityFirst = false)
public class ValidMockClass2 implements Cloneable{
    @AutoComparableTarget(priority = 3, order = Order.DESC)
    Integer field1;

    @AutoComparableTarget(priority = 4, order = Order.DESC)
    boolean field2;

    @AutoComparableTarget(priority = 2, order = Order.ASC, alternativeCompareMethod = "compare")
    List<Integer> field3;

    ValidMockClass2(Integer field1, boolean field2, List<Integer> field3) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
    }

    public static int compare(int[] a, int[] b){
        if (a == b)
            return 0;
        if (a == null || b == null)
            return a == null ? -1 : 1;

        int i = ArraysSupport.mismatch(a, b,
                Math.min(a.length, b.length));
        if (i >= 0) {
            return Integer.compare(a[i], b[i]);
        }

        return a.length - b.length;
    }

    public static int compare(List<Integer> a, List<Integer> b){
        if (a == b)
            return 0;
        if (a == null || b == null)
            return a == null ? -1 : 1;
        int j;
        for(int i=0; i<Math.min(a.size(),b.size()); i++){
            j = Integer.compare(a.get(i), b.get(i));
            if(j != 0)
                return j;
        }
        return a.size() - b.size();
    }

    @AutoComparableTarget(priority = 1, order = Order.ASC)
    public Integer sum(){
        return field3.stream().reduce(Integer::sum).orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidMockClass2 that = (ValidMockClass2) o;
        return field2 == that.field2 &&
                Objects.equals(field1, that.field1) &&
                Objects.equals(field3, that.field3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field1, field2, field3);
    }

    @Override
    public String toString() {
        return "ValidMockClass2{" +
                "field1=" + field1 +
                ", field2=" + field2 +
                ", field3=" + field3 +
                '}';
    }

    public static Comparator<ValidMockClass2> getExpectedComparator(){
        return new Comparator<ValidMockClass2>(){
            @Override
            public int compare(ValidMockClass2 o1, ValidMockClass2 o2) {
                return ValidMockClass2.expectedCompare(o1,o2);
            }
        };
    }

    private static int expectedCompare(ValidMockClass2 o1, ValidMockClass2 o2) {
        int e1 = Boolean.compare(o2.field2, o1.field2);
        if(e1 == 0){
            int e2 = Integer.compare(o2.field1, o1.field1);
            if( e2 == 0){
                int e3 = compare(o1.field3,o2.field3);
                if ( e3 == 0){
                    return Integer.compare(o1.sum(),o2.sum());
                }
                return e3;
            }
            return e2;
        }
        return e1;

    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new ValidMockClass2(field1, field2, field3);
    }
}
