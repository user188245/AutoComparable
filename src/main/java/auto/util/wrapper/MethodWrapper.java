package auto.util.wrapper;

import com.sun.source.tree.MethodTree;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;

/**
 * @param <T> the method syntax tree
 * @author user188245
 */
public class MethodWrapper<T> extends ASTWrapper<T> {

    protected MethodWrapper(T data) {
        super(data);
    }

    public static MethodWrapper<MethodTree> from(MethodTree data) {
        return new MethodWrapper<MethodTree>(data);
    }

    public static MethodWrapper<AbstractMethodDeclaration> from(AbstractMethodDeclaration data) {
        return new MethodWrapper<AbstractMethodDeclaration>(data);
    }

}
