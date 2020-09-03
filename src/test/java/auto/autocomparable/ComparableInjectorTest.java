package auto.autocomparable;

import auto.autocomparable.mock.AutoComparableTargetMissingMockClass;
import auto.autocomparable.mock.MockClassFactory;
import auto.autocomparable.mock.ValidMockClass1;
import com.sun.source.tree.CompilationUnitTree;
import org.junit.Before;
import org.junit.Test;
import auto.util.AnnotationProcessorToolFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ComparableInjectorTest {

    ComparableInjector comparableInjector;

    @Before
    public void before() {
        //todo
        this.comparableInjector = new ComparableInjector(AnnotationProcessorToolFactory.instance(ProcessingEnviromentFactory.build()));
    }

    public CompilationUnitTree classToCu(Class<?> cls){
        //todo
        return null;
    }


    @Test
    public void AutoComparableTargetMissingMockClassTest() {
        try{
            comparableInjector.process(classToCu(AutoComparableTargetMissingMockClass.class));
            fail();
        }catch(RuntimeException e2){
            assertEquals(IllegalArgumentException.class, e2.getClass());
        }
    }

    @Test
    public void ComparableImplementedMockClassTest() {
        //todo
    }

    @Test
    public void CompareToMethodAlreadyExistedMockClassTest() {
        //todo
    }

    @Test
    public void InvalidAutoComparableFieldMockClass1Test() {
        //todo
    }

    @Test
    public void InvalidAutoComparableFieldMockClass2Test() {
        //todo
    }

    @Test
    public void InvalidAutoComparableTargetParamMockClass1Test() {
        //todo
    }

    @Test
    public void InvalidAutoComparableTargetParamMockClass2Test() {
        //todo
    }

    @Test
    public void InvalidAutoComparableTargetParamMockClass3Test() {
        //todo
    }

    @Test
    @SuppressWarnings("all")
    public void ValidMockClass1Test() {
        try{
            CompilationUnitTree cu = comparableInjector.process(classToCu(ValidMockClass1.class));

            ValidMockClass1 cls= MockClassFactory.generateValidMockClass1();

            List<Comparable> actual = new ArrayList<Comparable>(10);
            List<ValidMockClass1> expected = new ArrayList<ValidMockClass1>(10);

            for(int i=0; i<10; i++) {
                cls = MockClassFactory.generateValidMockClass1();
                if (!(cls instanceof Comparable)) {
                    fail();
                }
                actual.add((Comparable) cls);
                expected.add(cls);
            }
            Collections.sort(actual);
            Collections.sort(expected,ValidMockClass1.getExpectedComparator());
            assertEquals(actual, expected);
        }catch(RuntimeException e2){
            fail();
        }
    }

    @Test
    public void ValidMockClass2Test() {
        //todo
    }

    @Test
    public void ValidMockClass3Test() {
        //todo
    }

}