package autocomparable.mock;

import autocomparable.annotation.AutoComparable;
import autocomparable.annotation.AutoComparableTarget;

@AutoComparable
public class InvalidAutoComparableTargetParamMockClass3 {

    @AutoComparableTarget(priority = 1, alternativeCompareMethod = "invalidCompare")
    int[] arrValue;

    public static int invalidCompare(int[] o1){
        return 0;
    }


}
