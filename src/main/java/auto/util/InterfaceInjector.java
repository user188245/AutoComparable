package auto.util;

import auto.util.wrapper.ClassWrapper;
import auto.util.wrapper.CompilationUnitWrapper;
import auto.util.wrapper.ImportWrapper;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.Field;

public abstract class InterfaceInjector implements CompilationUnitProcessor {

    protected AnnotationProcessorTool annotationProcessorTool;
    private Class<?> inf;
    private Field[] fields;
    TypeElement infType;
    private ASTPositionCorrector ASTPositionCorrector;

    public InterfaceInjector(Class<?> inf, AnnotationProcessorTool annotationProcessorTool) throws IllegalArgumentException{
        setAnnotationProcessorTool(annotationProcessorTool);
        if(!inf.isInterface()){
            throw new AnnotationProcessingException(ExceptionCode.INTERNAL_ERROR, inf.getName() + " isn't interface.");
        }
        this.inf = inf;
        this.fields = inf.getFields();
        this.infType = annotationProcessorTool.createTypeElement(inf);
        this.ASTPositionCorrector = ASTPositionCorrectorFactory.instance(annotationProcessorTool);
    }

    public void setAnnotationProcessorTool(AnnotationProcessorTool annotationProcessorTool) {
        this.annotationProcessorTool = annotationProcessorTool;
    }

    public Class<?> getInterface(){
        return this.inf;
    }

    public Field[] getFieldsShouldImplemented(){
        return this.fields;
    }

    @Override
    public CompilationUnitWrapper process(CompilationUnitWrapper compilationUnit) {
        ClassWrapper classWrapper = injectImportAndGetClass(compilationUnit);
        TypeElement te = annotationProcessorTool.extractTypeElement(classWrapper);
        if(containsInterfaceDuplication(te)){
            throw new AnnotationProcessingException(ExceptionCode.INTERFACE_EXIST, inf.getName() + " is already exists.");
        }
        injectInterface(classWrapper);
        processAfterInterfaceInjection(classWrapper);
        ASTPositionCorrector.correctPosition(classWrapper);
        return compilationUnit;
    }

    private ClassWrapper injectImportAndGetClass(CompilationUnitWrapper compilationUnit){
        ImportWrapper importWrapper = annotationProcessorTool.createImport(infType);
        return annotationProcessorTool.injectImport(compilationUnit,importWrapper);
    }

    protected void injectInterface(ClassWrapper classWrapper){
        annotationProcessorTool.injectInterface(classWrapper, infType);
    }

    protected TypeMirror getInfTypeMirror(TypeElement self){
        return infType.asType();
    }

    protected boolean containsInterfaceDuplication(TypeElement cls){
        TypeMirror infTypeMirror = getInfTypeMirror(cls);
        for(TypeMirror tm : cls.getInterfaces()){
            if(annotationProcessorTool.isSubtype(tm,infTypeMirror)){
                return true;
            }
        }
        return false;
    }

    abstract protected void processAfterInterfaceInjection(ClassWrapper classWrapper);
}
