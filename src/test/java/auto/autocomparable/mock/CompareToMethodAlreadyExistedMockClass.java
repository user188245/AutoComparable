package auto.autocomparable.mock;

import auto.autocomparable.annotation.AutoComparable;
import auto.autocomparable.annotation.AutoComparableTarget;

import java.util.Objects;

@AutoComparable
public class CompareToMethodAlreadyExistedMockClass {

    @AutoComparableTarget(priority = 300)
    int value;

    CompareToMethodAlreadyExistedMockClass(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompareToMethodAlreadyExistedMockClass that = (CompareToMethodAlreadyExistedMockClass) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public int compareTo(CompareToMethodAlreadyExistedMockClass o) {
        return Integer.compare(this.value,o.value);
    }
}
