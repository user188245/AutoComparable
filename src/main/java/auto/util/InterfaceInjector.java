package auto.util;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.Field;

import static javax.lang.model.element.ElementKind.CLASS;

public abstract class InterfaceInjector implements CompilationUnitProcessor {

    protected AnnotationProcessorTool annotationProcessorTool;
    private Class<?> inf;
    private Field[] fields;
    TypeElement infType;


    public InterfaceInjector(Class<?> inf, AnnotationProcessorTool annotationProcessorTool) throws IllegalArgumentException{
        setAnnotationProcessorTool(annotationProcessorTool);
        if(!inf.isInterface()){
            throw new IllegalArgumentException(inf.getName() + " isn't interface.");
        }
        this.inf = inf;
        this.fields = inf.getFields();
        this.infType = annotationProcessorTool.createTypeElement(inf);
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
    public CompilationUnitTree process(CompilationUnitTree compilationUnit) {
        ClassTree classTree = injectImportAndGetClass(compilationUnit);
        TypeElement te = annotationProcessorTool.extractTypeElement(classTree);
        if(containsInterfaceDuplication(te)){
            throw new IllegalArgumentException();
        }
        injectInterface(classTree);
        processAfterInterfaceInjection(classTree);
        return compilationUnit;
    }

    private ClassTree injectImportAndGetClass(CompilationUnitTree compilationUnit){
        ImportTree importTree = annotationProcessorTool.createImport(infType);
        return annotationProcessorTool.injectImport(compilationUnit,importTree);
    }

    protected void injectInterface(ClassTree classTree){
        annotationProcessorTool.injectInterface(classTree, infType);
    }

    protected TypeMirror getInfTypeMirror(TypeElement cls){
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

    abstract protected void processAfterInterfaceInjection(ClassTree classTree);
}
