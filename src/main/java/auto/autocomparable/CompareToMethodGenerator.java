package autocomparable;

import autocomparable.annotation.Order;
import com.sun.source.tree.MethodTree;
import util.AnnotationProcessorTool;
import util.MethodGenerator;

import java.util.List;

class CompareToMethodGenerator implements MethodGenerator {
    private List<ComparableTarget> targets;
    private Order priorityOrder;
    private AnnotationProcessorTool apt;

    public CompareToMethodGenerator(List<ComparableTarget> targets, Order priorityOrder, AnnotationProcessorTool annotationProcessorTool) {
        this.targets = targets;
        this.priorityOrder = priorityOrder;
        this.apt = annotationProcessorTool;
    }

    @Override
    public MethodTree generateMethod() {
        //todo

        return null;
    }
}
