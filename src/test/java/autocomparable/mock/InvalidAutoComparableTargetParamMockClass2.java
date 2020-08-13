package autocomparable.mock;

import autocomparable.annotation.AutoComparable;
import autocomparable.annotation.AutoComparableTarget;

@AutoComparable
public class InvalidAutoComparableTargetParamMockClass2 {

    @AutoComparableTarget(priority = 1, alternativeCompareMethod = "invalidCompare")
    int[] arrValue;

    public int invalidCompare(long[] o1, long[] o2){
        return 0;
    }


}
