package auto.util.wrapper;

import com.sun.source.tree.Tree;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;

/**
 * @author user188245
 * @param <T> the abstract syntax tree
 */
public class ASTWrapper<T> extends Wrapper<T> {

    protected ASTWrapper(T data) {
        super(data);
    }

    public static ASTWrapper<Tree> from(Tree data){
        return new ASTWrapper<Tree>(data);
    }

    public static ASTWrapper<ASTNode> from(ASTNode data){
        return new ASTWrapper<ASTNode>(data);
    }

}
