package auto.autocomparable.mock;

import auto.autocomparable.annotation.AutoComparable;
import auto.autocomparable.annotation.AutoComparableTarget;

@AutoComparable
public class InvalidAutoComparableFieldMockClass2 {


    public InvalidAutoComparableFieldMockClass2() {
    }

    @AutoComparableTarget(priority = 1)
    public int invalid(int a){
        return a;
    }


}
