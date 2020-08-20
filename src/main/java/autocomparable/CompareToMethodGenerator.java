package autocomparable;

import autocomparable.annotation.Order;
import com.sun.source.tree.MethodTree;
import util.MethodGenerator;

import java.util.List;

class CompareToMethodGenerator implements MethodGenerator {
    private List<ComparableTarget> targets;
    private Order priorityOrder;

    public CompareToMethodGenerator(List<ComparableTarget> targets, Order priorityOrder) {
        this.targets = targets;
        this.priorityOrder = priorityOrder;
    }

    @Override
    public MethodTree generateMethod() {
        //todo
        return null;
    }
}
