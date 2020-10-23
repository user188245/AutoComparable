package auto.util.wrapper;

import com.sun.source.tree.BinaryTree;
import org.eclipse.jdt.internal.compiler.ast.BinaryExpression;

/**
 * @author user188245
 * @param <T> the binary-operation syntax tree
 */
public class BinaryWrapper<T> extends ExpressionWrapper<T> {

    protected BinaryWrapper(T data) {
        super(data);
    }

    public static BinaryWrapper<BinaryTree> from(BinaryTree data){
        return new BinaryWrapper<BinaryTree>(data);
    }

    public static BinaryWrapper<BinaryExpression> from(BinaryExpression data){
        return new BinaryWrapper<BinaryExpression>(data);
    }

}
