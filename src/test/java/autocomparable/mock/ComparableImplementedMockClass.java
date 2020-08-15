package autocomparable.mock;

import java.util.Objects;

public class ComparableImplementedMockClass implements Comparable<ComparableImplementedMockClass> {

    int value;

    ComparableImplementedMockClass(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComparableImplementedMockClass that = (ComparableImplementedMockClass) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(ComparableImplementedMockClass o) {
        return Integer.compare(this.value,o.value);
    }
}