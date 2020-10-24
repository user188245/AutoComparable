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
 *
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
        if (priorityOrder == Order.DESC) {
            Collections.sort(targets);
        } else {
            Collections.sort(targets, Collections.<ComparableTarget>reverseOrder());
        }
        this.apt = annotationProcessorTool;
        this.self = self;
        this.intType = apt.createPrimitiveType(TypeKind.INT);
    }

    @Override
    public MethodWrapper generateMethod() {
        List<VariableElement> paramVars = new LinkedList<>();
        paramVars.add(apt.createParameterElement(self.asType(), paramName, self));

        LinkedList<StatementWrapper> body = null;

        ExpressionWrapper var = apt.createMemberSelect(baseVar);
        VariableElement varElem = apt.createVariableElement(null, intType, baseVar, null);

        for (ComparableTarget target : targets) {

            // v = compare(this.?, o.?); or v# = this.?.compareTo(o.?);
            StatementWrapper assignment = generateAssignment(varElem, target);

            // if( v# == 0 ) then {Itr[v#-1]}
            IfWrapper ift = null;
            if (body != null) {
                ExpressionWrapper ifVar = apt.createMemberSelect(baseVar);
                ift = apt.createIf(
                        apt.createBinaryOperation(ifVar, apt.createLiteral(0), BinaryOperator.EQ),
                        apt.createBlock(null, body),
                        null);
            }
            body = new LinkedList<>();

            // body creation
            body.add(assignment);
            if (ift != null) {
                body.add(ift);
            }
        }

        if (body != null) {
            // int v;
            body.addFirst(apt.createVariable(varElem, null));

            // return v;
            ExpressionWrapper returnVar = apt.createMemberSelect(baseVar);
            body.addLast(apt.createReturn(returnVar));
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

    private StatementWrapper generateAssignment(VariableElement element, ComparableTarget comparableTarget) {
        ExpressionWrapper methodCall = generateMethodCall(comparableTarget);
        return apt.createAssignment(element, methodCall);
    }

    private MethodInvocationWrapper generateMethodCall(ComparableTarget comparableTarget) {
        String targetAccess = comparableTarget.getCompareTarget();

        ExpressionWrapper left;
        ExpressionWrapper right;

        String compareReceiver = comparableTarget.getCompareReceiver();

        ExpressionWrapper receiver = (compareReceiver == null) ? null : apt.createMemberSelect(compareReceiver);

        if (comparableTarget.getOrder() == Order.DESC) {
            right = apt.createMemberSelect("this");
            left = apt.createMemberSelect(paramName);
        } else {
            left = apt.createMemberSelect("this");
            right = apt.createMemberSelect(paramName);
        }

        if (comparableTarget.getKind() == ComparableTarget.Kind.Method) {
            left = apt.createMethodInvocation(left, targetAccess, null);
            right = apt.createMethodInvocation(right, targetAccess, null);
        } else {
            left = apt.createMemberSelect(left, targetAccess);
            right = apt.createMemberSelect(right, targetAccess);
        }

        List<ExpressionWrapper> params = new LinkedList<>();
        if (comparableTarget.getMethodType() == ComparableTarget.MethodType.compare) {
            params.add(left);
            params.add(right);
            return apt.createMethodInvocation(receiver, comparableTarget.getCompareSelector(), params);
        } else {
            params.add(right);
            return apt.createMethodInvocation(left, comparableTarget.getCompareSelector(), params);
        }
    }
}
