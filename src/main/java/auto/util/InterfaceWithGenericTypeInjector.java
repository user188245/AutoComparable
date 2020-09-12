package auto.util;

import com.sun.source.tree.ClassTree;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class InterfaceWithGenericTypeInjector extends InterfaceInjector{

    private List<Class<?>> genericTypes;
    private List<TypeElement> genericTypeElements;
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
            if(cls != null) {
                genericTypeElements.add(annotationProcessorTool.createTypeElement(cls));
            }else{
                genericTypeElements.add(null);
            }
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
    protected TypeMirror getInfTypeMirror(TypeElement self) {
        TypeMirror typeMirror = infType.asType();
        List<TypeElement> genericsTypeElementsWithSubstitute = createGenericTypeElementsWithSubstitute(genericTypeElements,self);
        List<TypeMirror> genericsTypeMirrors = new LinkedList<>();
        genericsTypeElementsWithSubstitute.forEach(x->genericsTypeMirrors.add(x.asType()));
        return annotationProcessorTool.createGenericTypeMirror(typeMirror,genericsTypeMirrors);
    }

    @Override
    public final void injectInterface(ClassTree classTree) {
        List<TypeElement> genericsTypeElementsWithSubstitute = createGenericTypeElementsWithSubstitute(genericTypeElements,annotationProcessorTool.extractTypeElement(classTree));
        annotationProcessorTool.injectInterface(classTree, infType, genericsTypeElementsWithSubstitute);
    }

    private static List<TypeElement> createGenericTypeElementsWithSubstitute(List<TypeElement> genericTypeElements, TypeElement substitute){
        List<TypeElement> result = new ArrayList<>(genericTypeElements.size());
        genericTypeElements.forEach(x-> result.add(x==null?substitute:x));
        return result;
    }
}
