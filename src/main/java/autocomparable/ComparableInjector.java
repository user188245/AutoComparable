package autocomparable;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import util.AnnotationProcessorTool;
import util.InterfaceWithGenericTypeInjector;

import java.util.ArrayList;
import java.util.List;

public class ComparableInjector extends InterfaceWithGenericTypeInjector {

    public ComparableInjector(AnnotationProcessorTool annotationProcessorTool){
        super(Comparable.class, ComparableInjector.createGenericTypes(), annotationProcessorTool);
    }

    private static List<Class<?>> createGenericTypes(){
        List<Class<?>> genericTypes = new ArrayList<>(1);
        genericTypes.add(null); // Self Types
        return genericTypes;
    }

    @Override
    protected CompilationUnitTree processAfterInterfaceInjection(ClassTree classTree) {
        //todo
        return null;
    }
}
