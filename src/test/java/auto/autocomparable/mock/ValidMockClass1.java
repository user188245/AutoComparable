package auto.autocomparable.mock;

import auto.autocomparable.annotation.AutoComparable;
import auto.autocomparable.annotation.AutoComparableTarget;
import auto.autocomparable.annotation.Order;

import java.util.Comparator;
import java.util.Objects;

@AutoComparable
public class ValidMockClass1 implements Cloneable{
    @AutoComparableTarget(priority = 1, order = Order.DESC)
    int field1;

    @AutoComparableTarget(priority = 2, order = Order.ASC)
    long field2;

    ValidMockClass1(int field1, long field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidMockClass1 that = (ValidMockClass1) o;
        return field1 == that.field1 &&
                field2 == that.field2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(field1, field2);
    }

    @Override
    public String toString() {
        return "ValidMockClass1{" +
                "field1=" + field1 +
                ", field2=" + field2 +
                '}';
    }

    public static Comparator<ValidMockClass1> getExpectedComparator() {
            return new Comparator<ValidMockClass1>() {
                @Override
                public int compare(final ValidMockClass1 o1, final ValidMockClass1 o2) {
                    return ValidMockClass1.expectedCompare(o1,o2);
                }
            };
    }

    public static int expectedCompare(ValidMockClass1 o1,ValidMockClass1 o2) {
        int e1 = Integer.compare(o2.field1,o1.field1);
        if(e1 == 0){
            return Long.compare(o1.field2,o2.field2);
        }
        return e1;
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new ValidMockClass1(field1,field2);
    }
}
