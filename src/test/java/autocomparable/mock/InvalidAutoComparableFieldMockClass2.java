package autocomparable.mock;

import autocomparable.annotation.AutoComparable;
import autocomparable.annotation.AutoComparableTarget;

@AutoComparable
public class InvalidAutoComparableFieldMockClass2 {


    public InvalidAutoComparableFieldMockClass2() {
    }

    @AutoComparableTarget(priority = 1)
    public int invalid(int a){
        return a;
    }


}
