package auto.util.wrapper;

import com.sun.source.tree.MethodInvocationTree;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;

public class MethodInvocationWrapper<T> extends ExpressionWrapper<T> {

    protected MethodInvocationWrapper(T data) {
        super(data);
    }

    public static MethodInvocationWrapper<MethodInvocationTree> from(MethodInvocationTree data){
        return new MethodInvocationWrapper<MethodInvocationTree>(data);
    }

    public static MethodInvocationWrapper<FieldReference> from(FieldReference data){
        return new MethodInvocationWrapper<FieldReference>(data);
    }
}
