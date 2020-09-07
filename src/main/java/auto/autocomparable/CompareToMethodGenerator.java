package auto.autocomparable;

import auto.autocomparable.annotation.Order;
import com.sun.source.tree.MethodTree;
import auto.util.AnnotationProcessorTool;
import auto.util.MethodGenerator;

import javax.lang.model.element.TypeElement;
import java.util.List;

class CompareToMethodGenerator implements MethodGenerator {
    private List<ComparableTarget> targets;
    private Order priorityOrder;
    private AnnotationProcessorTool apt;
    private TypeElement self;

    public CompareToMethodGenerator(List<ComparableTarget> targets, Order priorityOrder, AnnotationProcessorTool annotationProcessorTool, TypeElement self) {
        this.targets = targets;
        this.priorityOrder = priorityOrder;
        this.apt = annotationProcessorTool;
        this.self = self;
    }

    @Override
    //todo
    // 1. sort targets by the order 'priorityOrder'.
    // 2. build
    public MethodTree generateMethod() {


        return null;
    }
}
