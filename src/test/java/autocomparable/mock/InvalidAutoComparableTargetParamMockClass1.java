package autocomparable.mock;

import autocomparable.annotation.AutoComparable;
import autocomparable.annotation.AutoComparableTarget;

@AutoComparable
public class InvalidAutoComparableTargetParamMockClass1 {

    @AutoComparableTarget(priority = 1, alternativeCompareMethod = "invalidCompare")
    int[] arrValue;

    public void invalidCompare(int[] o1, int[] o2){
        //do nothing
    }


}
