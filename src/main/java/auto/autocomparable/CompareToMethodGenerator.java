package auto.autocomparable;

import auto.autocomparable.annotation.Order;
import auto.util.BinaryOperator;
import com.sun.source.tree.*;
import auto.util.AnnotationProcessorTool;
import auto.util.MethodGenerator;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.sql.Statement;
import java.util.*;

class CompareToMethodGenerator implements MethodGenerator {
    private List<ComparableTarget> targets;
    private AnnotationProcessorTool apt;
    private TypeElement self;

    private static final String paramName = "o";
    private static final String baseVar = "v";

    private TypeMirror intType;

    CompareToMethodGenerator(List<ComparableTarget> targets, Order priorityOrder, AnnotationProcessorTool annotationProcessorTool, TypeElement self) {
        this.targets = targets;
        if(priorityOrder == Order.DESC){
            targets.sort(Collections.reverseOrder());
        }else{
            Collections.sort(targets);
        }
        this.apt = annotationProcessorTool;
        this.self = self;
        this.intType = apt.createPrimitiveType(TypeKind.INT);
    }

    @Override
    //todo
    // 1. sort targets by the order 'priorityOrder'.
    // 2. build
    //   foreach item from List<ComparableTarget>
    //   int v0 = Assignment(item);
    //    if( vo == 0){
    //       next Items here
    //    }
    //    return v0;

    public MethodTree generateMethod() {

        List<VariableElement> paramVars = new LinkedList<>();
        paramVars.add(apt.createVariableElement(null, self.asType(), paramName, self));

        ExecutableElement methodPrototype = apt.createMethodPrototype(
                EnumSet.of(Modifier.PUBLIC),
                "compareTo",
                        intType,
                        paramVars,
                null,
                self);

        List<StatementTree> body = new LinkedList<>();
        BlockTree block = apt.createBlock(null, body);
        int i = 0;
        Iterator<ComparableTarget> itr = targets.iterator();
        ComparableTarget target;
        while((target = itr.next()) != null){

            List<StatementTree> newBody = null;

            String varName = baseVar + i++;
            ExpressionTree var = apt.createMemberSelect(varName);

            VariableTree assignment = generateAssignment(varName,target); // v# = compare(this.?, o.?);
            body.add(assignment);
            if(itr.hasNext()){
                newBody = new LinkedList<>();
                IfTree ift = apt.createIf(apt.createBinaryOperation(var,apt.createLiteral(0), BinaryOperator.EQ), apt.createBlock(null, newBody), null); // if( v# == 0) then Next
                body.add(ift);
            }
            ReturnTree rtn = apt.createReturn(var); // return v#;
            body.add(rtn);

            if(newBody != null){
                body = newBody;
            }
        }
        return apt.createMethod(null, methodPrototype, block); // @Generated, @Override will be added.
    }


    //todo
    private VariableTree generateAssignment(String variable, ComparableTarget comparableTarget){
        ExpressionTree methodCall = generateMethodCall(comparableTarget);
        return apt.createVariable(null, intType, variable, methodCall, null);
    }

    //todo
    private MethodInvocationTree generateMethodCall(ComparableTarget comparableTarget){

        StringBuilder targetAccess = new StringBuilder(comparableTarget.getCompareTarget());

        if(comparableTarget.getKind() == ComparableTarget.Kind.Method){
            targetAccess.append("()");
        }

        ExpressionTree left = apt.createMemberSelect("this" + "." + targetAccess);
        ExpressionTree right = apt.createMemberSelect(paramName + "." + targetAccess);

        if(comparableTarget.getOrder() == Order.DESC){
            ExpressionTree swap;
            swap = left;
            left = right;
            right = swap;
        }

        ExpressionTree methodSelect;
        List<ExpressionTree> params = new LinkedList<>();
        if(comparableTarget.getMethodType() == ComparableTarget.MethodType.Compare){
            params.add(left);
            params.add(right);
            methodSelect = apt.createMemberSelect(comparableTarget.getCompareMethod());
        }else{
            params.add(right);
            methodSelect = apt.createMemberSelect(left + "." + comparableTarget.getCompareMethod());
        }
        return apt.createMethodInvocation(methodSelect,params);
    }

}
