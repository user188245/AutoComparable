package auto.util.wrapper;

import com.sun.source.tree.ExpressionTree;
import org.eclipse.jdt.internal.compiler.ast.Expression;

public class ExpressionWrapper<T> extends ASTWrapper<T> {

    protected ExpressionWrapper(T data) {
        super(data);
    }

    public static ExpressionWrapper<ExpressionTree> from(ExpressionTree data){
        return new ExpressionWrapper<ExpressionTree>(data);
    }

    public static ExpressionWrapper<Expression> from(Expression data){
        return new ExpressionWrapper<Expression>(data);
    }

}
