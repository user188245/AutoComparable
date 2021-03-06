package auto.util;

import auto.util.wrapper.ClassWrapper;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code InterfaceWithGenericTypeInjector} can deal with an interface which require type parameter. (it called generics)
 *
 * @author user188245
 * @see InterfaceInjector
 */
public abstract class InterfaceWithGenericTypeInjector extends InterfaceInjector {

    private List<Class<?>> genericTypes;
    private List<TypeElement> genericTypeElements;

    /**
     * @param inf                     the interface
     * @param genericTypes            the list of generic type defined at the interface, a null element will be recognized as 'Self-Type Class'
     * @param annotationProcessorTool the annotation processor tool which depends on specific compiler.
     * @throws AnnotationProcessingException throw if generic is incorrect with the type parameters of interface or arguments is invalid.
     */
    public InterfaceWithGenericTypeInjector(Class<?> inf, List<Class<?>> genericTypes, AnnotationProcessorTool annotationProcessorTool) throws AnnotationProcessingException {
        super(inf, annotationProcessorTool);
        int countOfGenerics = genericTypes.size();
        if (inf.getTypeParameters().length != countOfGenerics) {
            throw new AnnotationProcessingException(ExceptionCode.INTERNAL_ERROR, "The number of generic types must be equivalent to the number of type parameters defined by interface prototype");
        }
        this.genericTypes = genericTypes;
        this.genericTypeElements = new ArrayList<TypeElement>(genericTypes.size());
        for (Class<?> cls : genericTypes) {
            if (cls != null) {
                genericTypeElements.add(annotationProcessorTool.createTypeElement(cls));
            } else {
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
        List<TypeElement> genericsTypeElementsWithSubstitute = createGenericTypeElementsWithSubstitute(genericTypeElements, self);
        TypeMirror[] genericsTypeMirrors = new TypeMirror[genericsTypeElementsWithSubstitute.size()];
        int i = 0;
        for (TypeElement x : genericsTypeElementsWithSubstitute) {
            genericsTypeMirrors[i++] = x.asType();
        }
        return annotationProcessorTool.createGenericTypeMirror(infType, genericsTypeMirrors);
    }

    @Override
    public final void injectInterface(ClassWrapper classWrapper) {
        List<TypeElement> genericsTypeElementsWithSubstitute = createGenericTypeElementsWithSubstitute(genericTypeElements, annotationProcessorTool.extractTypeElement(classWrapper));
        annotationProcessorTool.injectInterface(classWrapper, infType, genericsTypeElementsWithSubstitute);
    }

    private static List<TypeElement> createGenericTypeElementsWithSubstitute(List<TypeElement> genericTypeElements, TypeElement substitute) {
        List<TypeElement> result = new ArrayList<TypeElement>(genericTypeElements.size());
        for (TypeElement x : genericTypeElements) {
            result.add(x == null ? substitute : x);
        }
        return result;
    }
}
