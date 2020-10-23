package auto.util.wrapper;

import com.sun.source.tree.IfTree;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;

/**
 * @author user188245
 * @param <T> the if syntax tree
 */
public class IfWrapper<T> extends StatementWrapper<T> {

    protected IfWrapper(T data) {
        super(data);
    }

    public static IfWrapper<IfTree> from(IfTree data){
        return new IfWrapper<IfTree>(data);
    }

    public static IfWrapper<IfStatement> from(IfStatement data){
        return new IfWrapper<IfStatement>(data);
    }
}
