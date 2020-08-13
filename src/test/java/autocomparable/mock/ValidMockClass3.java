package autocomparable.mock;

import autocomparable.annotation.AutoComparable;
import autocomparable.annotation.AutoComparableTarget;

import java.util.Objects;

@AutoComparable(isLowPriorityFirst = false)
public class ValidMockClass3 {

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
}
