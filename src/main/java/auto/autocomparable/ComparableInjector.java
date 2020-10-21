package auto.autocomparable;

import auto.autocomparable.annotation.AutoComparable;
import auto.autocomparable.annotation.AutoComparableTarget;
import auto.autocomparable.annotation.Order;
import auto.util.AnnotationProcessingException;
import auto.util.AnnotationProcessorTool;
import auto.util.InterfaceWithGenericTypeInjector;
import auto.util.MethodGenerator;
import auto.util.wrapper.ClassWrapper;
import auto.util.wrapper.MethodWrapper;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

public class ComparableInjector extends InterfaceWithGenericTypeInjector {

    private TypeMirror comparableType = null;

    public ComparableInjector(AnnotationProcessorTool annotationProcessorTool){
        super(Comparable.class, ComparableInjector.createGenericTypes(), annotationProcessorTool);
        if(comparableType == null){
            comparableType = annotationProcessorTool.createRawType(annotationProcessorTool.createTypeElement(getInterface()).asType());
        }
    }

    private static List<Class<?>> createGenericTypes(){
        List<Class<?>> genericTypes = new ArrayList<Class<?>>(1);
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
            throw new AnnotationProcessingException(AutoComparableExceptionCode.INVALID_AUTO_COMPARABLE_TARGET, "Only method or field variable are allowed as a @AutoComparableTarget");
        }
        ComparableTarget.Kind kind = ComparableTarget.Kind.Field;
        ComparableTarget.MethodType type = ComparableTarget.MethodType.compare;
        TypeMirror compareTargetType;
        String compareTargetName;
        if(e instanceof ExecutableElement){
            kind = ComparableTarget.Kind.Method;
            ExecutableElement ee = ((ExecutableElement)e);
            if(!ee.getParameters().isEmpty()){
                throw new AnnotationProcessingException(AutoComparableExceptionCode.INVALID_AUTO_COMPARABLE_TARGET, "The method using @AutoComparableTarget must not have a parameter.");
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
                compareReceiver = alternativeMethodName.substring(0,dot);
                compareSelector = alternativeMethodName.substring(dot+1);
            }
            try {
                boolean exactlyMethodExist = false;
                TypeElement alternativeClass = (compareReceiver==null)? (TypeElement) e.getEnclosingElement() :annotationProcessorTool.createTypeElement(Class.forName(compareReceiver));
                AnnotationProcessingException hold = null;
                ExecutableElement alternativeMethod = null;
                for(Element e2 : alternativeClass.getEnclosedElements()){
                    if(e2.getSimpleName().toString().equals(compareSelector)){
                        if(e2 instanceof ExecutableElement){
                            alternativeMethod = (ExecutableElement)e2;
                            if(alternativeMethod.getReturnType().getKind() != TypeKind.INT){
                                hold = new AnnotationProcessingException(AutoComparableExceptionCode.ILLEGAL_AUTO_COMPARABLE_TARGET_ARGUMENT, "The alternative method must return int.");
                                continue;
                            }
                            if(alternativeMethod.getParameters().size() != 2){
                                hold = new AnnotationProcessingException(AutoComparableExceptionCode.ILLEGAL_AUTO_COMPARABLE_TARGET_ARGUMENT, "The alternative method must have exactly 2 parameter.");
                                continue;
                            }
                            boolean isError = false;
                            for(VariableElement e3 : alternativeMethod.getParameters()){
                                if(!annotationProcessorTool.isSubtype(compareTargetType, e3.asType())){
                                    hold = new AnnotationProcessingException(AutoComparableExceptionCode.ILLEGAL_AUTO_COMPARABLE_TARGET_ARGUMENT, "The alternative method has Illegal parameters. Expected Parameter: " + compareTargetType + ", Actual Parameter: " + e3.asType());
                                    isError = true;
                                    break;
                                }
                            }
                            if(isError){
                                continue;
                            }
                            if(!alternativeMethod.getThrownTypes().isEmpty()){
                                hold = new AnnotationProcessingException(AutoComparableExceptionCode.ILLEGAL_AUTO_COMPARABLE_TARGET_ARGUMENT, "The alternative method must not have any of thrown.");
                                continue;
                            }
                            if(compareReceiver!=null && !annotationProcessorTool.isSubtype(alternativeClass.asType(), e.getEnclosingElement().asType()) && !alternativeMethod.getModifiers().contains(Modifier.STATIC)){
                                hold = new AnnotationProcessingException(AutoComparableExceptionCode.ILLEGAL_AUTO_COMPARABLE_TARGET_ARGUMENT, "The alternative method on external class must be static.");
                                continue;
                            }
                            exactlyMethodExist = true;
                            break;
                        }else{
                            hold = new AnnotationProcessingException(AutoComparableExceptionCode.ILLEGAL_AUTO_COMPARABLE_TARGET_ARGUMENT, "Alternative method is not executable.");
                        }
                    }
                }
                if(!exactlyMethodExist){
                    if(hold == null){
                        hold = new AnnotationProcessingException(AutoComparableExceptionCode.ILLEGAL_AUTO_COMPARABLE_TARGET_ARGUMENT, "Can't find an alternative method.");
                    }
                    throw hold;
                }
            } catch (ClassNotFoundException ex) {
                throw new AnnotationProcessingException(AutoComparableExceptionCode.ILLEGAL_AUTO_COMPARABLE_TARGET_ARGUMENT, "Can't find a class including alternative method.");
            }

        }else{
            compareReceiver = getPrimitiveCompareReceiver(compareTargetType);
            if(compareReceiver == null){
                if(!annotationProcessorTool.isSubtype(compareTargetType,comparableType) && annotationProcessorTool.extractAnnotations(compareTargetType,AutoComparable.class) == null){
                    // it is nether primitive nor Comparable nor AutoComparable.
                    throw new AnnotationProcessingException(AutoComparableExceptionCode.INVALID_AUTO_COMPARABLE_TARGET, "The return value of method or Type of field with @AutoComparableTarget must be comparable(or holds @AutoComparable)");
                }
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
        List<ComparableTarget> fields = new ArrayList<ComparableTarget>();

        // 3. Find Field member containing @AutoComparableTarget
        List<? extends Element> classFields = cls.getEnclosedElements();

        // 4. If a Field Member doesn't exist, CompileError
        if(classFields.isEmpty()){
            throw new AnnotationProcessingException(AutoComparableExceptionCode.INTERNAL_ERROR, "Missing Elements of class.");
        }


        for(Element e: classFields){
            if(e instanceof ExecutableElement){
                ExecutableElement ee = ((ExecutableElement)e);
                // 5. Check if there's a method which is not compatible with CompareTo(TargetClass cls) method for method overloading. if it exists, CompileError
                if(ee.getSimpleName().toString().equals("compareTo") && ee.getParameters().size() == 1 && annotationProcessorTool.isSameType(ee.getParameters().get(0).asType(),cls.asType())){
                    throw new AnnotationProcessingException(AutoComparableExceptionCode.METHOD_EXIST, "The abstract method '" + ee.getSimpleName() + "' is already implemented.");
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
            throw new AnnotationProcessingException(AutoComparableExceptionCode.MISSING_AUTO_COMPARABLE_TARGET, "@AutoComparable must have 1 @AutoComparableTarget at least.");
        }
        MethodGenerator methodGenerator = new CompareToMethodGenerator(fields,(autoComparable.isLowPriorityFirst())? Order.ASC:Order.DESC,annotationProcessorTool, cls);
        // 8. Generate CompareTo method by using CompareToMethodGenerator.
        MethodWrapper methodWrapper = methodGenerator.generateMethod();
        // 9. Inject the method.
        annotationProcessorTool.injectMethod(classWrapper,methodWrapper);
    }
}
