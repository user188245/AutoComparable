package auto.autocomparable.mock;

import auto.autocomparable.annotation.AutoComparable;
import auto.autocomparable.annotation.AutoComparableTarget;

import java.util.Arrays;

@AutoComparable
public class InvalidAutoComparableFieldMockClass1 {

    @AutoComparableTarget(priority = 1)
    int[] field;

    public InvalidAutoComparableFieldMockClass1(int[] field) {
        this.field = field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvalidAutoComparableFieldMockClass1 that = (InvalidAutoComparableFieldMockClass1) o;
        return Arrays.equals(field, that.field);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(field);
    }

    @Override
    public String toString() {
        return "InvalidAutoComparableFieldMockClass1{" +
                "field=" + Arrays.toString(field) +
                '}';
    }
}
