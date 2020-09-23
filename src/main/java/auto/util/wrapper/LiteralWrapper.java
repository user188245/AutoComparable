package auto.util.wrapper;


import com.sun.source.tree.LiteralTree;
import org.eclipse.jdt.internal.compiler.ast.Literal;

public class LiteralWrapper<T> extends ExpressionWrapper<T> {

    protected LiteralWrapper(T data) {
        super(data);
    }

    public static LiteralWrapper<LiteralTree> from(LiteralTree data){
        return new LiteralWrapper<LiteralTree>(data);
    }

    public static LiteralWrapper<Literal> from(Literal data){
        return new LiteralWrapper<Literal>(data);
    }

}
