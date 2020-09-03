package auto.util;

import com.sun.source.tree.CompilationUnitTree;

public interface CompilationUnitProcessor {

    CompilationUnitTree process(CompilationUnitTree compilationUnit);

}
