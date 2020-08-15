package autocomparable;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

abstract public class InterfaceWithGenericTypeInjector extends InterfaceInjector{

    private List<Class<?>> genericTypes;
    private List<TypeVariable<? extends Class<?>>> genericTypeParameters;

    InterfaceWithGenericTypeInjector(Class<?> inf, List<Class<?>> genericTypes, AnnotationProcessorTool annotationProcessorTool) throws IllegalArgumentException {
        super(inf, annotationProcessorTool);
        int countOfGenerics = genericTypes.size();
        if(inf.getTypeParameters().length != countOfGenerics){
            throw new IllegalArgumentException("The number of generic types must be equivalent to the number of type parameters defined by interface prototype");
        }
        genericTypeParameters = Arrays.asList(inf.getTypeParameters());
        this.genericTypes = genericTypes;
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

}
