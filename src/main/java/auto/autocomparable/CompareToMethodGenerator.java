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
            Collections.sort(targets);
        }else{
            targets.sort(Collections.reverseOrder());
        }
        this.apt = annotationProcessorTool;
        this.self = self;
        this.intType = apt.createPrimitiveType(TypeKind.INT);
    }

    @Override
    public MethodTree generateMethod() {

        List<VariableElement> paramVars = new LinkedList<>();
        paramVars.add(apt.createVariableElement(null, self.asType(), paramName, self,true));

        ExecutableElement methodPrototype = apt.createMethodPrototype(
                EnumSet.of(Modifier.PUBLIC),
                "compareTo",
                        intType,
                        paramVars,
                null,
                self);

        List<StatementTree> body = null;
        int i = 0;
        for(ComparableTarget target : targets){

            //base
            String varName = baseVar + i++;
            ExpressionTree var = apt.createMemberSelect(varName);

            // init
            VariableTree assignment = generateAssignment(varName,target); // v# = compare(this.?, o.?); or v# = this.?.compareTo(o.?);

            // if
            IfTree ift = null;
            if(body != null){
                ift = apt.createIf(
                    apt.createBinaryOperation(var,apt.createLiteral(0),
                    BinaryOperator.EQ),
                    apt.createBlock(null, body),
            null); // if( v# == 0 ) then {Itr[v#-1]}
            }
            body = null;
            body = new LinkedList<>();

            // return
            ReturnTree rtn = apt.createReturn(var); // return v#;

            // body creation
            body.add(assignment);
            if(ift != null){
                body.add(ift);
            }
            body.add(rtn);

        }
        BlockTree block = apt.createBlock(null, body);
        return apt.createMethod(null, methodPrototype, block); // @Generated, @Override will be added.
    }

    private VariableTree generateAssignment(String variable, ComparableTarget comparableTarget){
        ExpressionTree methodCall = generateMethodCall(comparableTarget);
        VariableElement variableElement = apt.createVariableElement(null,intType, variable, null,false);
        return apt.createVariable(variableElement, methodCall);
    }

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
