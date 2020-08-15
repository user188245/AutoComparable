package autocomparable;

import java.util.ArrayList;
import java.util.List;

public class ComparableInjector extends InterfaceWithGenericTypeInjector {

    ComparableInjector(List<Class<?>> genericTypes, AnnotationProcessorTool annotationProcessorTool){
        super(Comparable.class, genericTypes, annotationProcessorTool);
    }

    private List<Class<?>> createGenericTypes(){
        List<Class<?>> genericTypes = new ArrayList<>(1);
        genericTypes.add(null); // Self Types
        return genericTypes;
    }

    @Override
    protected void processAfterInterfaceInjection() {

    }
}
