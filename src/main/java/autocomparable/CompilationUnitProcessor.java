package autocomparable;

import com.sun.source.tree.CompilationUnitTree;

public interface CompilationUnitProcessor {

    void process(CompilationUnitTree compilationUnit);

}
