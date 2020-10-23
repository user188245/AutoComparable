package auto.autocomparable;

import auto.autocomparable.annotation.Order;

/**
 *
 * @author user188245
 */
class ComparableTarget implements Comparable<ComparableTarget>{

    enum Kind{
        Field,
        Method
    }

    enum MethodType{
        compareTo,
        compare
    }

    private Kind kind;
    private MethodType methodType;
    private int priority;
    private Order order;
    private String compareTarget;
    private String compareReceiver;
    private String compareSelector;

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

    String getCompareReceiver() {
        return compareReceiver;
    }

    String getCompareSelector() {
        return compareSelector;
    }

    ComparableTarget(Kind kind, MethodType methodType, int priority, Order order, String compareTarget, String compareReceiver, String compareSelector) {
        this.kind = kind;
        this.methodType = methodType;
        this.priority = priority;
        this.order = order;
        this.compareTarget = compareTarget;
        this.compareReceiver = compareReceiver;
        if(compareSelector != null){
            this.compareSelector = compareSelector;
        }else{
            this.compareSelector = methodType.name();
        }
    }

    @Override
    public int compareTo(ComparableTarget o) {
        return Integer.compare(this.priority,o.priority);
    }
}
