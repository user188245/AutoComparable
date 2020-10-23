package auto.util.wrapper;

import com.sun.source.tree.StatementTree;
import org.eclipse.jdt.internal.compiler.ast.Statement;

/**
 * @author user188245
 * @param <T> the statement syntax tree
 */
public class StatementWrapper<T> extends ASTWrapper<T>{

    protected StatementWrapper(T data) {
        super(data);
    }

    public static StatementWrapper<StatementTree> from(StatementTree data){
        return new StatementWrapper<StatementTree>(data);
    }

    public static StatementWrapper<Statement> from(Statement data){
        return new StatementWrapper<Statement>(data);
    }
}
