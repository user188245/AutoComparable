package autocomparable;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.List;

//todo
public interface AnnotationProcessorTool {

    ProcessingEnvironment getProcessingEnvironment();

    TypeElement createTypeElement(Class<?> inf);

    ImportTree createImport(TypeElement e);

    void injectImport(CompilationUnitTree compilationUnitTree, ImportTree importTree);

    void injectInterface(CompilationUnitTree compilationUnitTree, TypeElement infType, List<TypeElement> genericTypes);


}
