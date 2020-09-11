package auto.util;

import com.sun.source.tree.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

//todo
public interface AnnotationProcessorTool {

    ProcessingEnvironment getProcessingEnvironment();


    // creation

    TypeElement createTypeElement(Class<?> cls);

    VariableElement createVariableElement(Set<Modifier> modifiers, TypeMirror varType, String varName, TypeElement from);

    VariableElement createParameterElement(TypeMirror varType, String varName, TypeElement from);

    ImportTree createImport(TypeElement e);

    ModifiersTree createModifier(List<AnnotationTree> annotations, Set<Modifier> modifiers);

    VariableTree createVariable(VariableElement variableElement, ExpressionTree init);

    MethodInvocationTree createMethodInvocation(ExpressionTree methodExpr, List<ExpressionTree> args);

    ReturnTree createReturn(ExpressionTree expr);

    IfTree createIf(ExpressionTree condExpr, StatementTree thenExpr, StatementTree elseExpr);

    BinaryTree createBinaryOperation(ExpressionTree leftExpr, ExpressionTree rightExpr, BinaryOperator binaryOperator);

    TypeMirror createPrimitiveType(TypeKind kind);

    <A extends Annotation> AnnotationTree createAnnotation(A annotation);

    TypeMirror createGenericTypeMirror(TypeMirror prototype, List<TypeMirror> genericsTypes);

    MethodTree createMethod(List<AnnotationTree> annotations, ExecutableElement method, BlockTree block);

    ExecutableElement createMethodPrototype(Set<Modifier> modifiers, String name, TypeMirror returnType, List<VariableElement> params, List<TypeMirror> thrown, TypeElement from);

    BlockTree createBlock(Set<Modifier> modifiers, List<StatementTree> statements);

    ExpressionTree createMemberSelect(String fullPath);

    AssignmentTree createAssignment(ExpressionTree target, ExpressionTree value);

    LiteralTree createLiteral(Object obj);

    //verify

    boolean isSameType(TypeMirror t1, TypeMirror t2);

    boolean isSubtype(TypeMirror t1, TypeMirror t2);

    // injection

    ClassTree injectImport(CompilationUnitTree compilationUnitTree, ImportTree importTree);

    void injectInterface(ClassTree classTree, TypeElement infType);

    void injectInterface(ClassTree classTree, TypeElement infType, List<TypeElement> genericTypes);

    void injectMethod(ClassTree classTree, MethodTree methodTree);

    // extraction

    ClassTree extractTree(CompilationUnitTree compilationUnit);

    TypeElement extractTypeElement(ClassTree classTree);

    int extractPosition(Tree tree);

    // modify

    Tree setPosition(Tree tree, int pos);



}
