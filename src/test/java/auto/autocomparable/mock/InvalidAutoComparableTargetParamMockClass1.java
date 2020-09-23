package auto.autocomparable.mock;

import auto.autocomparable.annotation.AutoComparableTarget;

import java.util.Arrays;

//@AutoComparable
public class InvalidAutoComparableTargetParamMockClass1 {

    @AutoComparableTarget(priority = 1, alternativeCompareMethod = "invalidCompare")
    int[] arrValue;

    public void invalidCompare(int[] o1, int[] o2){
        //do nothing
    }

    @Override
    public String toString() {
        return "InvalidAutoComparableTargetParamMockClass1{" +
                "arrValue=" + Arrays.toString(arrValue) +
                '}';
    }

    public InvalidAutoComparableTargetParamMockClass1(int[] arrValue) {
        this.arrValue = arrValue;
    }
}
