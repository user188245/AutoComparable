package autocomparable;

import com.sun.source.tree.ClassTree;

import java.util.ArrayList;
import java.util.List;

public class ComparableInjector extends InterfaceWithGenericTypeInjector {


    ComparableInjector(List<Class<?>> genericTypes, AnnotationProcessorTool annotationProcessorTool){
        super(Comparable.class, ComparableInjector.createGenericTypes(), annotationProcessorTool);
    }

    private static List<Class<?>> createGenericTypes(){
        List<Class<?>> genericTypes = new ArrayList<>(1);
        genericTypes.add(null); // Self Types
        return genericTypes;
    }

    @Override
    protected void processAfterInterfaceInjection(ClassTree classTree) {
        //todo
    }
}
