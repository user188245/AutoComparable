package util;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;

import javax.lang.model.element.TypeElement;
import java.lang.reflect.Field;

public abstract class InterfaceInjector implements CompilationUnitProcessor {

    AnnotationProcessorTool annotationProcessorTool;
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
        injectInterface(classTree);
        return processAfterInterfaceInjection(classTree);
    }

    private ClassTree injectImportAndGetClass(CompilationUnitTree compilationUnit){
        ImportTree importTree = annotationProcessorTool.createImport(infType);
        return annotationProcessorTool.injectImport(compilationUnit,importTree);
    }

    protected void injectInterface(ClassTree classTree){
        annotationProcessorTool.injectInterface(classTree, infType, null);
    }

    abstract protected CompilationUnitTree processAfterInterfaceInjection(ClassTree classTree);
}
