package autocomparable.mock;

import autocomparable.annotation.AutoComparable;
import autocomparable.annotation.AutoComparableTarget;
import autocomparable.annotation.Order;
import jdk.internal.util.ArraysSupport;

import java.util.List;
import java.util.Objects;

@AutoComparable(isLowPriorityFirst = false)
public class ValidMockClass2{
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

    public int compare(int[] a, int[] b){
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

    public int compare(List<Integer> a, List<Integer> b){
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

    public class ExpectedValidMockClass2 extends ValidMockClass2 implements Comparable<ValidMockClass2>{

        ExpectedValidMockClass2(ValidMockClass2 validMockClass2) {
            super(validMockClass2.field1,validMockClass2.field2, validMockClass2.field3);
        }

        @Override
        public int compareTo(ValidMockClass2 o) {
            int e1 = Boolean.compare(o.field2, this.field2);
            if(e1 == 0){
                int e2 = Integer.compare(o.field1, this.field1);
                if( e2 == 0){
                    int e3 = compare(this.field3,o.field3);
                    if ( e3 == 0){
                        return Integer.compare(this.sum(),o.sum());
                    }
                    return e3;
                }
                return e2;
            }
            return e1;
        }
    }

    public static int expectedCompare(ValidMockClass1 o1,ValidMockClass1 o2) {
        int e1 = Integer.compare(o2.field1,o1.field1);
        if(e1 == 0){
            return Long.compare(o1.field2,o2.field2);
        }
        return e1;
    }
}
