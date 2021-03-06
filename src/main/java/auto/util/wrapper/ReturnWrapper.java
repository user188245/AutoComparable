package auto.util.wrapper;

import com.sun.source.tree.ReturnTree;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;

/**
 * @param <T> the return syntax tree
 * @author user188245
 */
public class ReturnWrapper<T> extends StatementWrapper<T> {

    protected ReturnWrapper(T data) {
        super(data);
    }

    public static ReturnWrapper<ReturnTree> from(ReturnTree data) {
        return new ReturnWrapper<ReturnTree>(data);
    }

    public static ReturnWrapper<ReturnStatement> from(ReturnStatement data) {
        return new ReturnWrapper<ReturnStatement>(data);
    }
}
