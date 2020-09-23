package auto.util;

import auto.util.wrapper.CompilationUnitWrapper;

public interface CompilationUnitProcessor {

    CompilationUnitWrapper process(CompilationUnitWrapper compilationUnit);

}
