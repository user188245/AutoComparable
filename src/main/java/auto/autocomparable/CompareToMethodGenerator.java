package auto.autocomparable;

import auto.autocomparable.annotation.Order;
import com.sun.source.tree.MethodTree;
import auto.util.AnnotationProcessorTool;
import auto.util.MethodGenerator;

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
