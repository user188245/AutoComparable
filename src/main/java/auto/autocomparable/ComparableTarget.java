package auto.autocomparable;

import auto.autocomparable.annotation.Order;
import com.sun.source.tree.MethodInvocationTree;

class ComparableTarget implements Comparable<ComparableTarget>{

    enum Kind{
        Field,
        Method
    }

    private Kind type;
    private int priority;
    private Order order;
    private MethodInvocationTree completedMethodInvocation;

    public ComparableTarget(Kind type, int priority, Order order, MethodInvocationTree completedMethodInvocation) {
        this.type = type;
        this.priority = priority;
        this.order = order;
        this.completedMethodInvocation = completedMethodInvocation;
    }

    public Kind getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }

    public Order getOrder() {
        return order;
    }

    public MethodInvocationTree getCompletedMethodInvocation() {
        return completedMethodInvocation;
    }

    @Override
    public int compareTo(ComparableTarget o) {
        return Integer.compare(this.priority,o.priority);
    }
}
