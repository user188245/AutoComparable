package auto.util.wrapper;

import com.sun.source.tree.MethodInvocationTree;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;

/**
 * @param <T> the method-call syntax tree
 * @author user188245
 */
public class MethodInvocationWrapper<T> extends ExpressionWrapper<T> {

    protected MethodInvocationWrapper(T data) {
        super(data);
    }

    public static MethodInvocationWrapper<MethodInvocationTree> from(MethodInvocationTree data) {
        return new MethodInvocationWrapper<MethodInvocationTree>(data);
    }

    public static MethodInvocationWrapper<MessageSend> from(MessageSend data) {
        return new MethodInvocationWrapper<MessageSend>(data);
    }
}
