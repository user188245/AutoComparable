package util;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.List;

//todo
public interface AnnotationProcessorTool {

    ProcessingEnvironment getProcessingEnvironment();


    // creation

    TypeElement createTypeElement(Class<?> cls);

    ImportTree createImport(TypeElement e);

    // injection

    void injectImport(CompilationUnitTree compilationUnitTree, ImportTree importTree);

    void injectInterface(ClassTree classTree, TypeElement infType, List<TypeElement> genericTypes);

    // extraction

    ClassTree extractTree(CompilationUnitTree compilationUnit);
}
