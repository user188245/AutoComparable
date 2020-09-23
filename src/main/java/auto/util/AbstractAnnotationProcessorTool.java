package auto.util;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

abstract class AbstractAnnotationProcessorTool implements AnnotationProcessorTool{

    private Elements elements;
    private Types types;

    public AbstractAnnotationProcessorTool(ProcessingEnvironment processingEnv) {
        this.elements = processingEnv.getElementUtils();
        this.types = processingEnv.getTypeUtils();
    }

    @Override
    public TypeElement createTypeElement(Class<?> cls) {
        return elements.getTypeElement(cls.getCanonicalName());
    }

    @Override
    public boolean isSameType(TypeMirror t1, TypeMirror t2) {
        return types.isSameType(t1,t2);
    }

    @Override
    public boolean isSubtype(TypeMirror t1, TypeMirror t2) {
        return types.isSubtype(t1,t2);
    }

    @Override
    public TypeMirror createPrimitiveType(TypeKind kind) {
        return types.getPrimitiveType(kind);
    }

}
