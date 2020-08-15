package autocomparable.mock;

import autocomparable.annotation.AutoComparable;
import autocomparable.annotation.AutoComparableTarget;
import autocomparable.annotation.Order;

import java.util.Objects;

@AutoComparable
public class ValidMockClass1{
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

    public class ExpectedValidMockClass1 extends ValidMockClass1 implements Comparable<ValidMockClass1>{

        ExpectedValidMockClass1(ValidMockClass1 validMockClass1) {
            super(validMockClass1.field1, validMockClass1.field2);
        }

        @Override
        public int compareTo(ValidMockClass1 o) {
            int e1 = Integer.compare(o.field1,this.field1);
            if(e1 == 0){
                return Long.compare(this.field2,o.field2);
            }
            return e1;
        }
    }

    public ExpectedValidMockClass1 extract() {
        return new ExpectedValidMockClass1(this);
    }
}
