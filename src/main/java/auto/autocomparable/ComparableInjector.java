package auto.autocomparable;

import auto.autocomparable.annotation.AutoComparable;
import auto.autocomparable.annotation.AutoComparableTarget;
import auto.autocomparable.annotation.Order;
import auto.util.AnnotationProcessorTool;
import auto.util.InterfaceWithGenericTypeInjector;
import auto.util.MethodGenerator;
import auto.util.wrapper.ClassWrapper;
import auto.util.wrapper.MethodWrapper;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

public class ComparableInjector extends InterfaceWithGenericTypeInjector {

    private static TypeMirror comparableType = null;

    public ComparableInjector(AnnotationProcessorTool annotationProcessorTool){
        super(Comparable.class, ComparableInjector.createGenericTypes(), annotationProcessorTool);
        if(comparableType == null){
            comparableType = annotationProcessorTool.createRawType(annotationProcessorTool.createTypeElement(getInterface()).asType());
        }
    }

    private static List<Class<?>> createGenericTypes(){
        List<Class<?>> genericTypes = new ArrayList<>(1);
        genericTypes.add(null); // Self Types
        return genericTypes;
    }

    private static String getPrimitiveCompareReceiver(TypeMirror type){
        switch(type.getKind()){
            case INT:
                return "Integer";
            case LONG:
                return "Long";
            case SHORT:
                return "Short";
            case BYTE:
                return "Byte";
            case CHAR:
                return "Character";
            case BOOLEAN:
                return "Boolean";
            case FLOAT:
                return "Float";
            case DOUBLE:
                return "Double";
            default:
                return null;
        }
    }

    private ComparableTarget getComparableTarget(Element e, AutoComparableTarget autoComparableTarget){
        if(e.getKind() != ElementKind.FIELD && e.getKind() != ElementKind.METHOD){
            throw new IllegalArgumentException();
        }
        ComparableTarget.Kind kind = ComparableTarget.Kind.Field;
        ComparableTarget.MethodType type = ComparableTarget.MethodType.compare;
        TypeMirror compareTargetType;
        String compareTargetName;
        if(e instanceof ExecutableElement){
            kind = ComparableTarget.Kind.Method;
            ExecutableElement ee = ((ExecutableElement)e);
            if(!ee.getParameters().isEmpty()){
                throw new IllegalArgumentException();
            }
            compareTargetType = ee.getReturnType();
        }else{
            compareTargetType = e.asType();
        }
        compareTargetName = e.getSimpleName().toString();

        String compareReceiver = null;
        String compareSelector = null;

        String alternativeMethodName = autoComparableTarget.alternativeCompareMethod();
        if(!alternativeMethodName.equals("")){
            int dot = alternativeMethodName.lastIndexOf('.');
            if(dot == -1){
                compareSelector = alternativeMethodName;
            }else{
                //todo
                // find a direct accessible method
                compareReceiver = alternativeMethodName.substring(0,dot);
                compareSelector = alternativeMethodName.substring(dot+1);
            }
        }else{
            compareReceiver = getPrimitiveCompareReceiver(compareTargetType);
            if(compareReceiver == null){
                //todo
                // fix the if statement in order that 'getAnnotation' function works well
//                if(!annotationProcessorTool.isSubtype(compareTargetType,comparableType) && e.getAnnotation(AutoComparable.class) == null){
//                    // it is nether primitive nor Comparable nor AutoComparable.
//                    throw new IllegalArgumentException();
//                }
                // compareTarget has the method "compareTo"
                type = ComparableTarget.MethodType.compareTo;
            }
        }
        return new ComparableTarget(
                kind,
                type,
                autoComparableTarget.priority(),
                autoComparableTarget.order(),
                compareTargetName,
                compareReceiver,
                compareSelector);
    }

    @Override
    protected void processAfterInterfaceInjection(ClassWrapper classWrapper) {

        // 1. Find @AutoComparable
        TypeElement cls = annotationProcessorTool.extractTypeElement(classWrapper);

        // 2. Get @AutoComparable.isLowPriorityFirst
        AutoComparable autoComparable = cls.getAnnotation(AutoComparable.class);
        List<ComparableTarget> fields = new ArrayList<>();

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
        }
        if(fields.isEmpty()){
            throw new IllegalArgumentException();
        }
        MethodGenerator methodGenerator = new CompareToMethodGenerator(fields,(autoComparable.isLowPriorityFirst())? Order.ASC:Order.DESC,annotationProcessorTool, cls);
        // 8. Generate CompareTo method by using CompareToMethodGenerator.
        MethodWrapper methodWrapper = methodGenerator.generateMethod();
        // 9. Inject the method.
        annotationProcessorTool.injectMethod(classWrapper,methodWrapper);
    }
}
