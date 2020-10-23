package auto.autocomparable;

import auto.autocomparable.annotation.processing.AutoComparableProcessor;
import auto.autocomparable.mock.*;
import auto.util.AnnotationProcessingException;
import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.fail;

public class AutoComparableTest {

    private CompilerTester eclipseTester;
    private CompilerTester javacTester;
    private List<Class<? extends Processor>> processors;

    @Before
    public void before() {
        processors = new LinkedList<Class<? extends Processor>>();
        processors.add(AutoComparableProcessor.class);
        eclipseTester = new CompilerTester(new EclipseCompiler()).setProcessors(processors);
        javacTester = new CompilerTester(ToolProvider.getSystemJavaCompiler()).setProcessors(processors);
    }
    private void checkError(Class<?> testClass, int expectedCode){
        List<Class<?>> testClasses = new ArrayList<>(1);
        testClasses.add(testClass);
        checkError(testClasses,expectedCode);
    }

    private void checkError(List<Class<?>> testClasses, int expectedCode){
        checkError(javacTester,testClasses,expectedCode);
        checkError(eclipseTester,testClasses,expectedCode);
    }

    private void checkNotError(Class<?> testClass){
        List<Class<?>> testClasses = new ArrayList<>(1);
        testClasses.add(testClass);
        checkNotError(testClasses);
    }

    private void checkNotError(List<Class<?>> testClasses){
        checkNotError(javacTester,testClasses);
//        checkNotError(eclipseTester,testClasses);
    }

    private void checkError(CompilerTester compilerTester, List<Class<?>> testClasses, int expectedCode){
        try {
            boolean passed = false;
            List<Diagnostic<? extends JavaFileObject>> diagnostics = compilerTester.doCompiles(testClasses);
            for(Diagnostic<? extends JavaFileObject> d : diagnostics){
                if(d.getKind() == Diagnostic.Kind.ERROR){
                    if(d.getMessage(Locale.getDefault()).contains(AnnotationProcessingException.getCodeWithString(expectedCode)))
                        passed = true;
                };
            }
            if(!passed){
                fail("No Error during compile.");
            }
        } catch (IOException e) {
            fail("IOException");
        }
    }

    private void checkNotError(CompilerTester compilerTester, List<Class<?>> testClasses){
        try {
            List<Diagnostic<? extends JavaFileObject>> diagnostics = compilerTester.doCompiles(testClasses);
            for(Diagnostic<? extends JavaFileObject> d : diagnostics){
                if(d.getKind() == Diagnostic.Kind.ERROR){
                    fail(d.getMessage(Locale.getDefault()));
                };
            }
        } catch (IOException e) {
            fail("IOException");
        }
    }

    @Test
    public void AutoComparableTargetMissingMockClassTest() {
        checkError(AutoComparableTargetMissingMockClass.class, AutoComparableExceptionCode.MISSING_AUTO_COMPARABLE_TARGET);
    }

    @Test
    public void ComparableImplementedMockClassTest() {
        checkError(ComparableImplementedMockClass.class, AutoComparableExceptionCode.INTERFACE_EXIST);
    }

    @Test
    public void CompareToMethodAlreadyExistedMockClassTest() {
        checkError(CompareToMethodAlreadyExistedMockClass.class, AutoComparableExceptionCode.METHOD_EXIST);
    }

    @Test
    public void InvalidAutoComparableFieldMockClass1Test() {
        checkError(InvalidAutoComparableFieldMockClass1.class, AutoComparableExceptionCode.INVALID_AUTO_COMPARABLE_TARGET);
    }

    @Test
    public void InvalidAutoComparableFieldMockClass2Test() {
        checkError(InvalidAutoComparableFieldMockClass2.class, AutoComparableExceptionCode.INVALID_AUTO_COMPARABLE_TARGET);
    }

    @Test
    public void InvalidAutoComparableTargetParamMockClass1Test() {
        checkError(InvalidAutoComparableTargetParamMockClass1.class, AutoComparableExceptionCode.ILLEGAL_AUTO_COMPARABLE_TARGET_ARGUMENT);
    }

    @Test
    public void InvalidAutoComparableTargetParamMockClass2Test() {
        checkError(InvalidAutoComparableTargetParamMockClass2.class, AutoComparableExceptionCode.ILLEGAL_AUTO_COMPARABLE_TARGET_ARGUMENT);
    }

    @Test
    public void InvalidAutoComparableTargetParamMockClass3Test() {
        checkError(InvalidAutoComparableTargetParamMockClass3.class, AutoComparableExceptionCode.ILLEGAL_AUTO_COMPARABLE_TARGET_ARGUMENT);
    }

    @Test
    public void ValidMockClass1Test() {
        checkNotError(ValidMockClass1.class);
    }

    @Test
    public void ValidMockClass2Test() {
        checkNotError(ValidMockClass2.class);
    }

    @Test
    public void ValidMockClass3Test() {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        classes.add(ValidMockClass1.class);
        classes.add(ValidMockClass3.class);
        checkNotError(classes);
    }

    @Test
    public void ValidMockClass4Test() {
        checkNotError(ValidMockClass4.class);
    }

}