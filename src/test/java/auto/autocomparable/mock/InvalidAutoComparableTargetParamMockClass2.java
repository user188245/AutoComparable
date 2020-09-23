package auto.autocomparable.mock;

import auto.autocomparable.annotation.AutoComparableTarget;

import java.util.Arrays;

//@AutoComparable
public class InvalidAutoComparableTargetParamMockClass2 {

    @AutoComparableTarget(priority = 1, alternativeCompareMethod = "invalidCompare")
    int[] arrValue;

    public int invalidCompare(long[] o1, long[] o2){
        return 0;
    }

    @Override
    public String toString() {
        return "InvalidAutoComparableTargetParamMockClass2{" +
                "arrValue=" + Arrays.toString(arrValue) +
                '}';
    }

    public InvalidAutoComparableTargetParamMockClass2(int[] arrValue) {
        this.arrValue = arrValue;
    }
}
