package auto.util.wrapper;

import com.sun.source.tree.CompilationUnitTree;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;

/**
 * @author user188245
 * @param <T> the compilation unit syntax tree
 */
public class CompilationUnitWrapper<T> extends ASTWrapper<T> {

    protected CompilationUnitWrapper(T data) {
        super(data);
    }

    public static CompilationUnitWrapper<CompilationUnitTree> from(CompilationUnitTree data){
        return new CompilationUnitWrapper<CompilationUnitTree>(data);
    }

    public static CompilationUnitWrapper<CompilationUnitDeclaration> from(CompilationUnitDeclaration data){
        return new CompilationUnitWrapper<CompilationUnitDeclaration>(data);
    }

}
