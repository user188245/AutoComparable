package auto.autocomparable.mock;

import auto.autocomparable.annotation.AutoComparable;
import auto.autocomparable.annotation.AutoComparableTarget;
import auto.autocomparable.util.ArrayStaticComparator;

import java.util.Arrays;

@AutoComparable
public class ValidMockClass4 implements Cloneable {

    @AutoComparableTarget(priority = 1, alternativeCompareMethod = "auto.autocomparable.util.ArrayStaticComparator.compareArrayByInnerJoiningMethod")
    private Integer[] field1;

    @AutoComparableTarget(priority = 2, alternativeCompareMethod = "auto.autocomparable.util.ArrayStaticComparator.compareArrayByInnerJoiningMethod")
    private String[] field2;

    public ValidMockClass4(Integer[] field1, String[] field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    public static int expectedCompare(ValidMockClass4 o1, ValidMockClass4 o2) {
        int v1 = ArrayStaticComparator.compareArrayByInnerJoiningMethod(o1.field1, o2.field1);
        if (v1 == 0) {
            v1 = ArrayStaticComparator.compareArrayByInnerJoiningMethod(o1.field2, o2.field2);
        }
        return v1;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new ValidMockClass4(Arrays.copyOf(field1, field1.length), Arrays.copyOf(field2, field2.length));
    }
}
