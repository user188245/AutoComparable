package auto.util.wrapper;

import com.sun.source.tree.VariableTree;
import org.eclipse.jdt.internal.compiler.ast.AbstractVariableDeclaration;

/**
 * @param <T> the variable syntax tree
 * @author user188245
 */
public class VariableWrapper<T> extends StatementWrapper<T> {

    protected VariableWrapper(T data) {
        super(data);
    }

    public static VariableWrapper<VariableTree> from(VariableTree data) {
        return new VariableWrapper<VariableTree>(data);
    }

    public static VariableWrapper<AbstractVariableDeclaration> from(AbstractVariableDeclaration data) {
        return new VariableWrapper<AbstractVariableDeclaration>(data);
    }

}
