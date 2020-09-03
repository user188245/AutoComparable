package auto.autocomparable;

import auto.autocomparable.annotation.AutoComparable;
import auto.autocomparable.annotation.AutoComparableTarget;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import auto.util.AnnotationProcessorTool;
import auto.util.InterfaceWithGenericTypeInjector;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.LinkedList;
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
        // 1. Find @AutoComparable
        // 2. Get @AutoComparable.isLowPriorityFirst
        // 3. Find Field member containing @AutoComparableTarget
        // 4. If a Field Member doesn't exist, CompileError
        // 5. Check if there's a method which is not compatible with CompareTo(TargetClass cls) method for method overloading. if it exists, CompileError
        // 6. Check if each @AutoComparableTarget is valid.
        //      Target must be Method or Field
        //      Field must be primitive type (not void) or Comparable (or super types of Comparable) or Containing @AutoComparable or Enum Type or use external compareTo method explicitly
        //      Method must satisfy 'Field Conditions' and has no parameters.
        // 7. Collect All @AutoComparableTarget Members. Build List<ComparableTarget>
        // 8. Generate CompareTo method by using CompareToMethodGenerator.
        // 9. Inject the method.

        Element cls = annotationProcessorTool.extractTypeElement(classTree);

        AutoComparable autoComparable = cls.getAnnotation(AutoComparable.class);
        List<Element> fields = new LinkedList<Element>();

        for(Element e: cls.getEnclosedElements()){
            AutoComparableTarget autoComparableTarget = e.getAnnotation(AutoComparableTarget.class);
            if(autoComparableTarget != null){
//                ComparableTarget ct = new ComparableTarget()
            }
        }




        return null;
    }

}
