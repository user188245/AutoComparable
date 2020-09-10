package auto.autocomparable.mock;

import auto.autocomparable.annotation.AutoComparable;
import auto.autocomparable.annotation.AutoComparableTarget;

import java.util.Arrays;

//@AutoComparable
public class InvalidAutoComparableTargetParamMockClass3 {

    @AutoComparableTarget(priority = 1, alternativeCompareMethod = "invalidCompare")
    int[] arrValue;

    public static int invalidCompare(int[] o1){
        return 0;
    }

    @Override
    public String toString() {
        return "InvalidAutoComparableTargetParamMockClass3{" +
                "arrValue=" + Arrays.toString(arrValue) +
                '}';
    }

    public InvalidAutoComparableTargetParamMockClass3(int[] arrValue) {
        this.arrValue = arrValue;
    }
}
