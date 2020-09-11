package auto.autocomparable;

import auto.autocomparable.annotation.Order;

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
    private String compareTarget;
    private String compareMethod;

    Kind getKind() {
        return kind;
    }

    MethodType getMethodType() {
        return methodType;
    }

    Order getOrder() {
        return order;
    }

    String getCompareTarget() {
        return compareTarget;
    }

    String getCompareMethod() {
        return compareMethod;
    }

    ComparableTarget(Kind kind, MethodType methodType, int priority, Order order, String compareTarget, String compareMethod) {
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
