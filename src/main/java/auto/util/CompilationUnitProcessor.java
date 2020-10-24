package auto.util;

import auto.util.wrapper.CompilationUnitWrapper;

/**
 * {@code CompilationUnitProcessor} provides an ability of handling the Abstract Syntax Tree(AST) at annotation processor
 *
 * @author user188245
 */
public interface CompilationUnitProcessor {

    /**
     * Process the compilation unit. the method should work compiler-independently.
     * if the method is invoked by an annotation processor running on unsupported compiler, throw UnsupportedCompilerException.
     *
     * @param compilationUnit The editable compilation unit tree.
     * @return self instance
     */
    CompilationUnitWrapper process(CompilationUnitWrapper compilationUnit);

}
