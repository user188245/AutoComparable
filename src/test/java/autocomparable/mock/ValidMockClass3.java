package autocomparable.mock;

import autocomparable.annotation.AutoComparable;
import autocomparable.annotation.AutoComparableTarget;

import java.util.Comparator;
import java.util.Objects;

@AutoComparable(isLowPriorityFirst = false)
public class ValidMockClass3 implements Cloneable{

    @AutoComparableTarget(priority = 100)
    ValidMockClass1 field1;

    @AutoComparableTarget(priority = 500)
    MockEnum field2;

    int field3;

    ValidMockClass3(ValidMockClass1 field1, MockEnum field2, int field3) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidMockClass3 that = (ValidMockClass3) o;
        return field3 == that.field3 &&
                Objects.equals(field1, that.field1) &&
                field2 == that.field2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(field1, field2, field3);
    }

    @Override
    public String toString() {
        return "ValidMockClass3{" +
                "field1=" + field1 +
                ", field2=" + field2 +
                ", field3=" + field3 +
                '}';
    }


    public static Comparator<ValidMockClass3> getExpectedComparator(){
        return new Comparator<ValidMockClass3>() {
            @Override
            public int compare(ValidMockClass3 o1, ValidMockClass3 o2) {
                return ValidMockClass3.expectedCompare(o1,o2);
            }
        };
    }

    private static int expectedCompare(ValidMockClass3 o1, ValidMockClass3 o2) {
        int e1 = o1.field2.ordinal() - o2.field2.ordinal();
        if(e1 == 0){
            return ValidMockClass1.expectedCompare(o1.field1,o2.field1);
        }
        return 0;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new ValidMockClass3(field1, field2, field3);
    }
}
