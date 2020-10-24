package auto.util;

import auto.util.wrapper.ClassWrapper;
import auto.util.wrapper.CompilationUnitWrapper;
import auto.util.wrapper.ImportWrapper;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.Field;

/**
 * {@code InterfaceInjector} provides automatic implementation of interface by using {@code AnnotationProcessorTool}
 *
 * @author user188245
 * @see AnnotationProcessorTool
 */
public abstract class InterfaceInjector implements CompilationUnitProcessor {

    protected AnnotationProcessorTool annotationProcessorTool;
    private Class<?> inf;
    private Field[] fields;
    TypeElement infType;

    /**
     * @param inf                     the interface
     * @param annotationProcessorTool the annotation processor tool which depends on specific compiler.
     * @throws AnnotationProcessingException thrown if the argument isn't interface
     */
    public InterfaceInjector(Class<?> inf, AnnotationProcessorTool annotationProcessorTool) throws AnnotationProcessingException {
        setAnnotationProcessorTool(annotationProcessorTool);
        if (!inf.isInterface()) {
            throw new AnnotationProcessingException(ExceptionCode.INTERNAL_ERROR, inf.getName() + " isn't interface.");
        }
        this.inf = inf;
        this.fields = inf.getFields();
        this.infType = annotationProcessorTool.createTypeElement(inf);
    }

    public void setAnnotationProcessorTool(AnnotationProcessorTool annotationProcessorTool) {
        this.annotationProcessorTool = annotationProcessorTool;
    }

    public Class<?> getInterface() {
        return this.inf;
    }

    /**
     * return the methods which should be implemented.
     *
     * @return the methods which should be implemented.
     */
    public Field[] getFieldsShouldImplemented() {
        return this.fields;
    }

    @Override
    public CompilationUnitWrapper process(CompilationUnitWrapper compilationUnit) {
        beforeProcess(compilationUnit);
        ClassWrapper classWrapper = injectImportAndGetClass(compilationUnit);
        TypeElement te = annotationProcessorTool.extractTypeElement(classWrapper);
        if (containsInterfaceDuplication(te)) {
            throw new AnnotationProcessingException(ExceptionCode.INTERFACE_EXIST, inf.getName() + " is already exists.");
        }
        injectInterface(classWrapper);
        processAfterInterfaceInjection(classWrapper);
        afterProcess(compilationUnit);
        return compilationUnit;
    }

    /**
     * pre-process the compilation unit before processing
     *
     * @param compilationUnit self instance
     */
    protected void beforeProcess(CompilationUnitWrapper compilationUnit) {
        //do nothing
    }

    ;

    /**
     * post-process the compilation unit after processing
     *
     * @param compilationUnit self instance
     */
    protected void afterProcess(CompilationUnitWrapper compilationUnit) {
        //do nothing
    }

    ;

    private ClassWrapper injectImportAndGetClass(CompilationUnitWrapper compilationUnit) {
        ImportWrapper importWrapper = annotationProcessorTool.createImport(infType);
        return annotationProcessorTool.injectImport(compilationUnit, importWrapper);
    }

    /**
     * inject the interface into the argument
     *
     * @param classWrapper the argument interface will be injected
     */
    protected void injectInterface(ClassWrapper classWrapper) {
        annotationProcessorTool.injectInterface(classWrapper, infType);
    }

    /**
     * @param self the self reference class. it used if a generic type is equivalent with the self class
     * @return the type of interface.
     */
    protected TypeMirror getInfTypeMirror(TypeElement self) {
        return infType.asType();
    }

    /**
     * verify the duplication of class.
     *
     * @param cls the target argument
     * @return true if the argument implements interface twice or more times, otherwise false.
     */
    protected boolean containsInterfaceDuplication(TypeElement cls) {
        TypeMirror infTypeMirror = getInfTypeMirror(cls);
        for (TypeMirror tm : cls.getInterfaces()) {
            if (annotationProcessorTool.isSubtype(tm, infTypeMirror)) {
                return true;
            }
        }
        return false;
    }

    /**
     * implement the abstract methods defined at interface.
     * <p>
     * The process can be skipped if the arguments class is abstract.
     *
     * @param classWrapper the class tree enclosed with compilation unit tree.
     */
    abstract protected void processAfterInterfaceInjection(ClassWrapper classWrapper);
}
