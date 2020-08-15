package autocomparable;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;

import javax.lang.model.element.TypeElement;
import java.lang.reflect.Field;

abstract class InterfaceInjector implements CompilationUnitProcessor {

    private AnnotationProcessorTool annotationProcessorTool;
    private Class<?> inf;
    private Field[] fields;
    private TypeElement infType;


    InterfaceInjector(Class<?> inf, AnnotationProcessorTool annotationProcessorTool) throws IllegalArgumentException{
        setAnnotationProcessorTool(annotationProcessorTool);
        if(!this.inf.isInterface()){
            throw new IllegalArgumentException(inf.getName() + " isn't interface.");
        }
        this.inf = inf;
        this.fields = inf.getFields();
        this.infType = annotationProcessorTool.createTypeElement(inf);
    }


    @Override
    public AnnotationProcessorTool getAnnotationProcessorTool() {
        return this.annotationProcessorTool;
    }

    @Override
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
    public void process(CompilationUnitTree compilationUnit) {
        injectImport(compilationUnit);
        injectInterface(compilationUnit);
        processAfterInterfaceInjection();
    }

    private void injectImport(CompilationUnitTree compilationUnit){
        ImportTree importTree = annotationProcessorTool.createImport(infType);
        annotationProcessorTool.injectImport(compilationUnit,importTree);
    }

    private void injectInterface(CompilationUnitTree compilationUnit){
        //todo
    }

    abstract protected void processAfterInterfaceInjection();
}
