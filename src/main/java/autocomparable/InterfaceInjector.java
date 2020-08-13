package autocomparable;

import java.lang.reflect.Field;

abstract class InterfaceInjector implements CompilationUnitProcessor {

    private AnnotationProcessorTool annotationProcessorTool;
    private Class<?> inf;
    private Field[] fields;


    InterfaceInjector(Class<?> inf, AnnotationProcessorTool annotationProcessorTool) throws IllegalArgumentException{
        setAnnotationProcessorTool(annotationProcessorTool);
        if(!this.inf.isInterface()){
            throw new IllegalArgumentException(inf.getName() + " isn't interface.");
        }
        this.inf = inf;
        this.fields = inf.getFields();
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


}
