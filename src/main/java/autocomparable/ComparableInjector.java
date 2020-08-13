package autocomparable;

import com.sun.source.tree.CompilationUnitTree;

public class ComparableInjector extends InterfaceInjector {

    public ComparableInjector(AnnotationProcessorTool annotationProcessorTool){
        super(Comparable.class, annotationProcessorTool);
    }


    @Override
    public void process(CompilationUnitTree compilationUnit) {
        //todo
    }
}
