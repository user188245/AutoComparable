package util;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.List;

//todo
class AnnotationProcessorToolImpl implements AnnotationProcessorTool {

    private ProcessingEnvironment processingEnv;

    AnnotationProcessorToolImpl(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    @Override
    public ProcessingEnvironment getProcessingEnvironment() {
        return null;
    }

    @Override
    public TypeElement createTypeElement(Class<?> cls) {
        return null;
    }

    @Override
    public ImportTree createImport(TypeElement e) {
        return null;
    }

    @Override
    public void injectImport(CompilationUnitTree compilationUnitTree, ImportTree importTree) {}

    @Override
    public void injectInterface(ClassTree classTree, TypeElement infType, List<TypeElement> genericTypes) {}

    @Override
    public ClassTree extractTree(CompilationUnitTree compilationUnit) {
        return null;
    }
}
