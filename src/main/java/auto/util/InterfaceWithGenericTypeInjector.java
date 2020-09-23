package auto.util;

import auto.util.wrapper.ClassWrapper;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class InterfaceWithGenericTypeInjector extends InterfaceInjector{

    private List<Class<?>> genericTypes;
    private List<TypeElement> genericTypeElements;

    public InterfaceWithGenericTypeInjector(Class<?> inf, List<Class<?>> genericTypes, AnnotationProcessorTool annotationProcessorTool) throws IllegalArgumentException {
        super(inf, annotationProcessorTool);
        int countOfGenerics = genericTypes.size();
        if(inf.getTypeParameters().length != countOfGenerics){
            throw new IllegalArgumentException("The number of generic types must be equivalent to the number of type parameters defined by interface prototype");
        }
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
        for(TypeElement x : genericsTypeElementsWithSubstitute){
            genericsTypeMirrors.add(x.asType());
        }
        return annotationProcessorTool.createGenericTypeMirror(typeMirror,genericsTypeMirrors);
    }

    @Override
    public final void injectInterface(ClassWrapper classWrapper) {
        List<TypeElement> genericsTypeElementsWithSubstitute = createGenericTypeElementsWithSubstitute(genericTypeElements,annotationProcessorTool.extractTypeElement(classWrapper));
        annotationProcessorTool.injectInterface(classWrapper, infType, genericsTypeElementsWithSubstitute);
    }

    private static List<TypeElement> createGenericTypeElementsWithSubstitute(List<TypeElement> genericTypeElements, TypeElement substitute){
        List<TypeElement> result = new ArrayList<>(genericTypeElements.size());
        for(TypeElement x : genericTypeElements){
            result.add(x==null?substitute:x);
        }
        return result;
    }
}
