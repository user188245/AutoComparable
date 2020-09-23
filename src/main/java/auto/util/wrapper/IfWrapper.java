package auto.util.wrapper;

import com.sun.source.tree.IfTree;
import sun.tools.tree.IfStatement;

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
