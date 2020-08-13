package autocomparable.mock;

import autocomparable.annotation.AutoComparableTarget;
import autocomparable.annotation.Order;

import java.util.Objects;

public class AutoComparableMissingMockClass {
    @AutoComparableTarget(priority = 1, order = Order.DESC)
    int field1;

    @AutoComparableTarget(priority = 2, order = Order.ASC)
    long field2;

    AutoComparableMissingMockClass(int field1, long field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutoComparableMissingMockClass that = (AutoComparableMissingMockClass) o;
        return field1 == that.field1 &&
                field2 == that.field2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(field1, field2);
    }
}
