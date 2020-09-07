package auto.autocomparable;

import auto.autocomparable.annotation.Order;
import com.sun.source.tree.ExpressionTree;

class ComparableTarget implements Comparable<ComparableTarget>{

    enum Kind{
        Field,
        Method
    }

    enum MethodType{
        CompareTo,
        Compare
    }

    private Kind kind;
    private MethodType methodType;
    private int priority;
    private Order order;
    private ExpressionTree compareTarget;
    private ExpressionTree compareMethod;

    ComparableTarget(Kind kind, MethodType methodType, int priority, Order order, ExpressionTree compareTarget, ExpressionTree compareMethod) {
        this.kind = kind;
        this.methodType = methodType;
        this.priority = priority;
        this.order = order;
        this.compareTarget = compareTarget;
        this.compareMethod = compareMethod;
    }

    @Override
    public int compareTo(ComparableTarget o) {
        return Integer.compare(this.priority,o.priority);
    }
}
