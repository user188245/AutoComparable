package auto.util.wrapper;

import com.sun.source.tree.CompilationUnitTree;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;

/**
 * @param <T> the compilation unit syntax tree
 * @author user188245
 */
public class CompilationUnitWrapper<T> extends ASTWrapper<T> {

    protected CompilationUnitWrapper(T data) {
        super(data);
    }

    public static CompilationUnitWrapper<CompilationUnitTree> from(CompilationUnitTree data) {
        return new CompilationUnitWrapper<CompilationUnitTree>(data);
    }

    public static CompilationUnitWrapper<CompilationUnitDeclaration> from(CompilationUnitDeclaration data) {
        return new CompilationUnitWrapper<CompilationUnitDeclaration>(data);
    }

}
