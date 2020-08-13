package autocomparable.mock;


import autocomparable.annotation.AutoComparable;

import java.util.Objects;

@AutoComparable(isLowPriorityFirst = false)
public class AutoComparableTargetMissingMockClass {
    int field1;

    long field2;

    AutoComparableTargetMissingMockClass(int field1, long field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutoComparableTargetMissingMockClass that = (AutoComparableTargetMissingMockClass) o;
        return field1 == that.field1 &&
                field2 == that.field2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(field1, field2);
    }
}
