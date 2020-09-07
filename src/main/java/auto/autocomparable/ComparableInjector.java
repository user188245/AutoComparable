package auto.autocomparable;

import auto.autocomparable.annotation.AutoComparable;
import auto.autocomparable.annotation.AutoComparableTarget;
import auto.autocomparable.annotation.Order;
import auto.util.MethodGenerator;
import com.sun.source.tree.ClassTree;
import auto.util.AnnotationProcessorTool;
import auto.util.InterfaceWithGenericTypeInjector;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodTree;
import com.sun.tools.javac.tree.JCTree;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

public class ComparableInjector extends InterfaceWithGenericTypeInjector {

    private PrimitiveCompareMethod primitiveCompareMethod;
    private TypeMirror comparableType;

    public ComparableInjector(AnnotationProcessorTool annotationProcessorTool){
        super(Comparable.class, ComparableInjector.createGenericTypes(), annotationProcessorTool);
        this.primitiveCompareMethod = new PrimitiveCompareMethodImpl(annotationProcessorTool);
        this.comparableType = annotationProcessorTool.createTypeElement(getInterface()).asType();
    }

    private static List<Class<?>> createGenericTypes(){
        List<Class<?>> genericTypes = new ArrayList<>(1);
        genericTypes.add(null); // Self Types
        return genericTypes;
    }

    //todo
    private ComparableTarget getComparableTarget(Element e, AutoComparableTarget autoComparableTarget){
        if(!e.getKind().isField()){
            throw new IllegalArgumentException();
        }
        ComparableTarget.Kind kind = ComparableTarget.Kind.Field;
        ComparableTarget.MethodType type = ComparableTarget.MethodType.Compare;
        TypeMirror compareTargetType;
        String compareTargetName;
        if(e instanceof ExecutableElement){
            kind = ComparableTarget.Kind.Method;
            ExecutableElement ee = ((ExecutableElement)e);
            if(!ee.getParameters().isEmpty()){
                throw new IllegalArgumentException();
            }
            compareTargetType = ee.getReturnType();
            compareTargetName = ee.getSimpleName().toString();
        }else{
            if(e instanceof VariableElement){
                compareTargetName = ((VariableElement)e).getSimpleName().toString();
            }else{
                compareTargetName = e.getSimpleName().toString();
            }

            compareTargetType = e.asType();
        }

        ExpressionTree compareMethod;

        //todo
        // CompareTo method must invoke alternative(substitute) method instead. if it exists,
        String alternativeMethodName = autoComparableTarget.alternativeCompareMethod();
        if(!alternativeMethodName.equals("")){
            compareMethod = annotationProcessorTool.extractMemberSelect(alternativeMethodName);
            //todo
            // verify "compareMethod" (it must hold 2 parameter which is equal with compareTarget, int type as a return.)
            // but there's no way to find exact "method prototype" without explicit invocation by using java reflection.
            // Only is it possible on runtime environment.

        }else{
            //todo
            // Field must be primitive type (not void) or Comparable (or super types of Comparable) or Containing @AutoComparable or Enum Type or use external compareTo method explicitly
            compareMethod = primitiveCompareMethod.getPrimitiveMethodTree(compareTargetType);
            if(compareMethod == null){
                if(!annotationProcessorTool.isSubtype(compareTargetType,comparableType) && compareTargetType.getAnnotation(AutoComparable.class) == null){
                    // it is not both primitive and Comparable and AutoComparable.
                    throw new IllegalArgumentException();
                }
                // compareTarget has the method "compareTo"
                type = ComparableTarget.MethodType.CompareTo;
                compareMethod = annotationProcessorTool.extractMemberSelect("compareTo");
            }
        }

        //todo
        return new ComparableTarget(
                kind,
                type,
                autoComparableTarget.priority(),
                autoComparableTarget.order(),
                annotationProcessorTool.extractMemberSelect(compareTargetName),
                compareMethod);
    }

    @Override
    protected void processAfterInterfaceInjection(ClassTree classTree) {
        //todo
        // 1. Find @AutoComparable
        // 2. Get @AutoComparable.isLowPriorityFirst
        // 3. Find Field member containing @AutoComparableTarget
        // 4. If a Field Member doesn't exist, CompileError
        // 5. Check if there's a method which is not compatible with CompareTo(TargetClass cls) method for method overloading. if it exists, CompileError
        // 6. Check if each @AutoComparableTarget is valid.
        //      Target must be Method or Field
        //      Field must be primitive type (not void) or Comparable (or super types of Comparable) or Containing @AutoComparable or Enum Type or use external compareTo method explicitly
        //      Method must have "return value" which satisfy 'Field Conditions' and has no parameters.
        // 7. Collect All @AutoComparableTarget Members. Build List<ComparableTarget>
        // 8. Generate CompareTo method by using CompareToMethodGenerator.
        // 9. Inject the method.

        // 1. Find @AutoComparable
        TypeElement cls = annotationProcessorTool.extractTypeElement(classTree);

        // 2. Get @AutoComparable.isLowPriorityFirst
        AutoComparable autoComparable = cls.getAnnotation(AutoComparable.class);
        List<ComparableTarget> fields = new ArrayList<ComparableTarget>();

        // 3. Find Field member containing @AutoComparableTarget
        List<? extends Element> classFields = cls.getEnclosedElements();

        // 4. If a Field Member doesn't exist, CompileError
        if(classFields.isEmpty()){
            throw new IllegalArgumentException();
        }


        for(Element e: classFields){
            if(e instanceof ExecutableElement){
                ExecutableElement ee = ((ExecutableElement)e);
                // 5. Check if there's a method which is not compatible with CompareTo(TargetClass cls) method for method overloading. if it exists, CompileError
                if(ee.getSimpleName().toString().equals("compareTo") && ee.getParameters().size() == 1 && annotationProcessorTool.isSameType(ee.getParameters().get(0).asType(),cls.asType())){
                    throw new IllegalArgumentException();
                }
            }
            AutoComparableTarget autoComparableTarget = e.getAnnotation(AutoComparableTarget.class);
            if(autoComparableTarget != null){
                // 6. Check if each @AutoComparableTarget is valid.
                ComparableTarget ct = getComparableTarget(e, autoComparableTarget);
                // 7. Collect All @AutoComparableTarget Members. Build List<ComparableTarget>
                fields.add(ct);
            }
            MethodGenerator methodGenerator = new CompareToMethodGenerator(fields,(autoComparable.isLowPriorityFirst())? Order.ASC:Order.DESC,annotationProcessorTool, cls);
            // 8. Generate CompareTo method by using CompareToMethodGenerator.
            MethodTree methodTree = methodGenerator.generateMethod();
            // 9. Inject the method.
            annotationProcessorTool.injectMethod(classTree,methodTree);
        }
    }



}
