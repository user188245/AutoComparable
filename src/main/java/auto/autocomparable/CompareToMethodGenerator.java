package auto.autocomparable;

import auto.autocomparable.annotation.Order;
import auto.util.AnnotationProcessorTool;
import auto.util.BinaryOperator;
import auto.util.MethodGenerator;
import auto.util.wrapper.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

/**
 * {@code CompareToMethodGenerator} can create the concrete "CompareTo" method defined by Comparable.
 * @author user188245
 */
class CompareToMethodGenerator implements MethodGenerator {
    private List<ComparableTarget> targets;
    private AnnotationProcessorTool apt;
    private TypeElement self;

    private static final EnumSet<Modifier> methodModifiers = EnumSet.of(Modifier.PUBLIC);
    private static final String methodName = "compareTo";
    private static final String paramName = "o";
    private static final String baseVar = "v";

    private TypeMirror intType;

    CompareToMethodGenerator(List<ComparableTarget> targets, Order priorityOrder, AnnotationProcessorTool annotationProcessorTool, TypeElement self) {
        this.targets = targets;
        if(priorityOrder == Order.DESC){
            Collections.sort(targets);
        }else{
            Collections.sort(targets,Collections.<ComparableTarget>reverseOrder());
        }
        this.apt = annotationProcessorTool;
        this.self = self;
        this.intType = apt.createPrimitiveType(TypeKind.INT);
    }

    //todo
    // make it be changed to use only 1 local variable.
    @Override
    public MethodWrapper generateMethod() {
        List<VariableElement> paramVars = new LinkedList<>();
        paramVars.add(apt.createParameterElement(self.asType(), paramName, self));

        List<StatementWrapper> body = null;
        int i = 0;
        for(ComparableTarget target : targets){

            //base
            String varName = baseVar + i++;
            ExpressionWrapper var = apt.createMemberSelect(varName);
            ExpressionWrapper returnVar = apt.createMemberSelect(varName);

            // init
            VariableWrapper assignment = generateAssignment(varName,target); // v# = compare(this.?, o.?); or v# = this.?.compareTo(o.?);

            // if
            IfWrapper ift = null;
            if(body != null){
                ift = apt.createIf(
                    apt.createBinaryOperation(var,apt.createLiteral(0), BinaryOperator.EQ),
                    apt.createBlock(null, body),
            null); // if( v# == 0 ) then {Itr[v#-1]}
            }
            body = new LinkedList<>();

            // return
            ReturnWrapper rtn = apt.createReturn(returnVar); // return v#;

            // body creation
            body.add(assignment);
            if(ift != null){
                body.add(ift);
            }
            body.add(rtn);
        }
        BlockWrapper block = apt.createBlock(null, body);
        List<AnnotationWrapper> annotations = new LinkedList<>();
        annotations.add(apt.createOverrideAnnotation());
        return apt.createMethod(
                annotations,
                methodModifiers,
                methodName,
                intType,
                paramVars,
                null,
                block,
                self
        );
    }

    private VariableWrapper generateAssignment(String variable, ComparableTarget comparableTarget){
        ExpressionWrapper methodCall = generateMethodCall(comparableTarget);
        VariableElement variableElement = apt.createVariableElement(null,intType, variable, null);
        return apt.createVariable(variableElement, methodCall);
    }

    private MethodInvocationWrapper generateMethodCall(ComparableTarget comparableTarget){
        String targetAccess = comparableTarget.getCompareTarget();

        ExpressionWrapper left;
        ExpressionWrapper right;

        String compareReceiver = comparableTarget.getCompareReceiver();

        ExpressionWrapper receiver = (compareReceiver==null)?null:apt.createMemberSelect(compareReceiver);

        if(comparableTarget.getOrder() == Order.DESC){
            right = apt.createMemberSelect("this");
            left = apt.createMemberSelect(paramName);
        }else{
            left = apt.createMemberSelect("this");
            right = apt.createMemberSelect(paramName);
        }

        if(comparableTarget.getKind() == ComparableTarget.Kind.Method){
            left = apt.createMethodInvocation(left, targetAccess, null);
            right = apt.createMethodInvocation(right, targetAccess, null);
        }else{
            left = apt.createMemberSelect(left, targetAccess);
            right = apt.createMemberSelect(right, targetAccess);
        }

        List<ExpressionWrapper> params = new LinkedList<>();
        if(comparableTarget.getMethodType() == ComparableTarget.MethodType.compare){
            params.add(left);
            params.add(right);
            return apt.createMethodInvocation(receiver,comparableTarget.getCompareSelector(),params);
        }else{
            params.add(right);
            return apt.createMethodInvocation(left,comparableTarget.getCompareSelector(),params);
        }
    }
}
