package util;

import com.sun.source.tree.ClassTree;

import javax.lang.model.element.TypeElement;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class InterfaceWithGenericTypeInjector extends InterfaceInjector{

    private List<Class<?>> genericTypes;
    List<TypeElement> genericTypeElements;
    private List<TypeVariable<? extends Class<?>>> genericTypeParameters;

    public InterfaceWithGenericTypeInjector(Class<?> inf, List<Class<?>> genericTypes, AnnotationProcessorTool annotationProcessorTool) throws IllegalArgumentException {
        super(inf, annotationProcessorTool);
        int countOfGenerics = genericTypes.size();
        if(inf.getTypeParameters().length != countOfGenerics){
            throw new IllegalArgumentException("The number of generic types must be equivalent to the number of type parameters defined by interface prototype");
        }
        genericTypeParameters = Arrays.asList(inf.getTypeParameters());
        this.genericTypes = genericTypes;
        this.genericTypeElements = new ArrayList<>(genericTypes.size());
        for(Class<?> cls : genericTypes){
            genericTypeElements.add(annotationProcessorTool.createTypeElement(cls));
        }
    }

    public List<TypeVariable<? extends Class<?>>> getGenericTypeParameters() {
        return genericTypeParameters;
    }

    public List<Class<?>> getGenericTypes() {
        return genericTypes;
    }

    public void setGenericTypes(List<Class<?>> genericTypes) {
        this.genericTypes = genericTypes;
    }

    @Override
    public final void injectInterface(ClassTree classTree) {
        annotationProcessorTool.injectInterface(classTree, infType, genericTypeElements);
    }
}