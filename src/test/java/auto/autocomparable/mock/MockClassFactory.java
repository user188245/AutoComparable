package auto.autocomparable.mock;

import java.util.ArrayList;
import java.util.List;

public class MockClassFactory {

    public static int generateInt(){
        return (int)(Math.random()*Integer.MAX_VALUE);
    }

    public static long generateLong(){
        return (long)(Math.random()*Long.MAX_VALUE);
    }

    public static boolean generateBoolean(){
        return Math.random()<0.5;
    }

    public static MockEnum generateMockEnum(){
        MockEnum[] values = MockEnum.values();
        return values[(int)(Math.random()*values.length)];
    }

    public static int[] generateIntArray(int size){
        int[] result = new int[size];
        for(int i=0; i<size; i++){
            result[i] = generateInt();
        }
        return result;
    }

    public static List<Integer> generateIntList(int size){
        List<Integer> result = new ArrayList<Integer>(size);
        for(int i=0; i<size; i++){
             result.add(generateInt());
        }
        return result;
    }

    public static AutoComparableTargetMissingMockClass generateAutoComparableTargetMissingMockClass(){
        return new AutoComparableTargetMissingMockClass(generateInt(),generateLong());
    }

    public static ComparableImplementedMockClass generateComparableImplementedMockClass(){
        return new ComparableImplementedMockClass(generateInt());
    }

    public static CompareToMethodAlreadyExistedMockClass generateCompareToMethodAlreadyExistedMockClass(){
        return new CompareToMethodAlreadyExistedMockClass(generateInt());
    }

    public static InvalidAutoComparableFieldMockClass1 generateInvalidAutoComparableFieldMockClass1(){
        return new InvalidAutoComparableFieldMockClass1(generateIntArray(4));
    }

    public static InvalidAutoComparableFieldMockClass2 generateInvalidAutoComparableFieldMockClass2(){
        return new InvalidAutoComparableFieldMockClass2();
    }

    public static InvalidAutoComparableTargetParamMockClass1 generateInvalidAutoComparableTargetParamMockClass1(){
        return new InvalidAutoComparableTargetParamMockClass1(generateIntArray(4));
    }

    public static InvalidAutoComparableTargetParamMockClass2 generateInvalidAutoComparableTargetParamMockClass2(){
        return new InvalidAutoComparableTargetParamMockClass2(generateIntArray(4));
    }

    public static InvalidAutoComparableTargetParamMockClass3 generateInvalidAutoComparableTargetParamMockClass3(){
        return new InvalidAutoComparableTargetParamMockClass3(generateIntArray(4));
    }

    public static ValidMockClass1 generateValidMockClass1(){
        return new ValidMockClass1(generateInt(), generateLong());
    }

    public static ValidMockClass2 generateValidMockClass2(){
        return new ValidMockClass2(generateInt(), generateBoolean(), generateIntList(4));
    }

    public static ValidMockClass3 generateValidMockClass3(){
        return new ValidMockClass3(generateValidMockClass1(), generateMockEnum(), generateInt());
    }

}
