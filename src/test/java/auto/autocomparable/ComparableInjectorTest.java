package auto.autocomparable;

import auto.autocomparable.mock.*;
import auto.util.AnnotationProcessorToolFactory;
import auto.util.wrapper.CompilationUnitWrapper;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.Trees;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ComparableInjectorTest {

    private ComparableInjector comparableInjector;
    private Elements elements;
    private Trees trees;

    @Before
    public void before() {
        ProcessingEnvironment pe = ProcessingEnviromentFactory.build();
        this.comparableInjector = new ComparableInjector(AnnotationProcessorToolFactory.instance(pe));
        this.elements = pe.getElementUtils();
        this.trees = Trees.instance(pe);
    }

    public CompilationUnitTree classToCu(Class<?> cls){
        return trees.getPath(elements.getTypeElement(cls.getCanonicalName())).getCompilationUnit();
    }


    @Test
    public void AutoComparableTargetMissingMockClassTest() {
        try{
            comparableInjector.process(CompilationUnitWrapper.from(classToCu(AutoComparableTargetMissingMockClass.class)));
            fail();
        }catch(RuntimeException e2){
            assertEquals(IllegalArgumentException.class, e2.getClass());
        }
    }

    @Test
    public void ComparableImplementedMockClassTest() {
        try{
            comparableInjector.process(CompilationUnitWrapper.from(classToCu(ComparableImplementedMockClass.class)));
            fail();
        }catch(RuntimeException e2){
            assertEquals(IllegalArgumentException.class, e2.getClass());
        }
    }

    @Test
    public void CompareToMethodAlreadyExistedMockClassTest() {
        try{
            comparableInjector.process(CompilationUnitWrapper.from(classToCu(CompareToMethodAlreadyExistedMockClass.class)));
            fail();
        }catch(RuntimeException e2){
            assertEquals(IllegalArgumentException.class, e2.getClass());
        }
    }

    @Test
    public void InvalidAutoComparableFieldMockClass1Test() {
        try{
            comparableInjector.process(CompilationUnitWrapper.from(classToCu(InvalidAutoComparableFieldMockClass1.class)));
            fail();
        }catch(RuntimeException e2){
            assertEquals(IllegalArgumentException.class, e2.getClass());
        }
    }

    @Test
    public void InvalidAutoComparableFieldMockClass2Test() {
        try{
            comparableInjector.process(CompilationUnitWrapper.from(classToCu(InvalidAutoComparableFieldMockClass2.class)));
            fail();
        }catch(RuntimeException e2){
            assertEquals(IllegalArgumentException.class, e2.getClass());
        }
    }

    @Test
    public void InvalidAutoComparableTargetParamMockClass1Test() {
        try{
            comparableInjector.process(CompilationUnitWrapper.from(classToCu(InvalidAutoComparableTargetParamMockClass1.class)));
            fail();
        }catch(RuntimeException e2){
            assertEquals(IllegalArgumentException.class, e2.getClass());
        }
    }

    @Test
    public void InvalidAutoComparableTargetParamMockClass2Test() {
        try{
            comparableInjector.process(CompilationUnitWrapper.from(classToCu(InvalidAutoComparableTargetParamMockClass2.class)));
            fail();
        }catch(RuntimeException e2){
            assertEquals(IllegalArgumentException.class, e2.getClass());
        }
    }

    @Test
    public void InvalidAutoComparableTargetParamMockClass3Test() {
        try{
            comparableInjector.process(CompilationUnitWrapper.from(classToCu(InvalidAutoComparableTargetParamMockClass3.class)));
            fail();
        }catch(RuntimeException e2){
            assertEquals(IllegalArgumentException.class, e2.getClass());
        }
    }

    @Test
    @SuppressWarnings("all")
    public void ValidMockClass1Test() {
        try{
            comparableInjector.process(CompilationUnitWrapper.from(classToCu(ValidMockClass1.class)));

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
        }catch(RuntimeException e){
            fail();
        }
    }

    @Test
    @SuppressWarnings("all")
    public void ValidMockClass2Test() {
        try{
            comparableInjector.process(CompilationUnitWrapper.from(classToCu(ValidMockClass2.class)));

            ValidMockClass2 cls= MockClassFactory.generateValidMockClass2();

            List<Comparable> actual = new ArrayList<Comparable>(10);
            List<ValidMockClass2> expected = new ArrayList<ValidMockClass2>(10);

            for(int i=0; i<10; i++) {
                cls = MockClassFactory.generateValidMockClass2();
                if (!(cls instanceof Comparable)) {
                    fail();
                }
                actual.add((Comparable) cls);
                expected.add(cls);
            }
            Collections.sort(actual);
            Collections.sort(expected,ValidMockClass2.getExpectedComparator());
            assertEquals(actual, expected);
        }catch(RuntimeException e){
            fail();
        }
    }

    @Test
    @SuppressWarnings("all")
    public void ValidMockClass3Test() {
        try{
            comparableInjector.process(CompilationUnitWrapper.from(classToCu(ValidMockClass3.class)));

            ValidMockClass3 cls= MockClassFactory.generateValidMockClass3();

            List<Comparable> actual = new ArrayList<Comparable>(10);
            List<ValidMockClass3> expected = new ArrayList<ValidMockClass3>(10);

            for(int i=0; i<10; i++) {
                cls = MockClassFactory.generateValidMockClass3();
                if (!(cls instanceof Comparable)) {
                    fail();
                }
                actual.add((Comparable) cls);
                expected.add(cls);
            }
            Collections.sort(actual);
            Collections.sort(expected,ValidMockClass3.getExpectedComparator());
            assertEquals(actual, expected);
        }catch(RuntimeException e){
            fail();
        }
    }

}