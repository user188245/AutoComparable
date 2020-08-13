package autocomparable;

import com.sun.source.tree.CompilationUnitTree;

public interface CompilationUnitProcessor {

    AnnotationProcessorTool getAnnotationProcessorTool();

    void setAnnotationProcessorTool(AnnotationProcessorTool annotationProcessorTool);

    void process(CompilationUnitTree compilationUnit);

}
