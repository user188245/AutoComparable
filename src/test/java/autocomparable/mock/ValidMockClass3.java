package autocomparable.mock;

import autocomparable.annotation.AutoComparable;
import autocomparable.annotation.AutoComparableTarget;

import java.util.Objects;

@AutoComparable(isLowPriorityFirst = false)
public class ValidMockClass3{

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

    public class ExpectedValidMockClass3 extends ValidMockClass3 implements Comparable<ValidMockClass3>{

        ExpectedValidMockClass3(ValidMockClass3 validMockClass3) {
            super(validMockClass3.field1, validMockClass3.field2, validMockClass3.field3);
        }

        @Override
        public int compareTo(ValidMockClass3 o) {
            int e1 = this.field2.ordinal() - o.field2.ordinal();
            if(e1 == 0){
                return compare(this.field1,o.field1);
            }
            return 0;
        }


    }

    public static int compare(ValidMockClass1 o1, ValidMockClass1 o2) {
        int e1 = Integer.compare(o2.field1,o1.field1);
        if(e1 == 0){
            return Long.compare(o1.field2,o2.field2);
        }
        return e1;
    }
}
