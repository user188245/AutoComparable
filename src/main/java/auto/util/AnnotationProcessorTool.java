package auto.util;

import auto.util.wrapper.*;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * Useful processor tool Providing a modification, creation, extraction of AST(Abstract Syntax Tree)
 *
 * @author user188245
 */
public interface AnnotationProcessorTool {

    // creation

    TypeElement createTypeElement(Class<?> cls);

    TypeMirror createRawType(TypeMirror typeWithGenerics);

    VariableElement createVariableElement(Set<Modifier> modifiers, TypeMirror varType, String varName, TypeElement from);

    VariableElement createParameterElement(TypeMirror varType, String varName, TypeElement from);

    ImportWrapper createImport(TypeElement e);

    VariableWrapper createVariable(VariableElement variableElement, ExpressionWrapper init);

    StatementWrapper createAssignment(VariableElement variableElement, ExpressionWrapper rightExpr);

    MethodInvocationWrapper createMethodInvocation(ExpressionWrapper receiver, String selector, List<ExpressionWrapper> args);

    ReturnWrapper createReturn(ExpressionWrapper expr);

    IfWrapper createIf(ExpressionWrapper condExpr, StatementWrapper thenExpr, StatementWrapper elseExpr);

    BinaryWrapper createBinaryOperation(ExpressionWrapper leftExpr, ExpressionWrapper rightExpr, BinaryOperator binaryOperator);

    TypeMirror createPrimitiveType(TypeKind kind);

    TypeMirror createGenericTypeMirror(TypeElement prototype, TypeMirror... genericsTypes);

    MethodWrapper createMethod(List<AnnotationWrapper> annotations, Set<Modifier> modifiers, String name, TypeMirror returnType, List<VariableElement> params, List<TypeMirror> thrown, BlockWrapper block, TypeElement from);

    BlockWrapper createBlock(Set<Modifier> modifiers, List<StatementWrapper> statements);

    ExpressionWrapper createMemberSelect(String fullPath);

    ExpressionWrapper createMemberSelect(ExpressionWrapper start, String fullPath);

    LiteralWrapper createLiteral(Object obj);

    AnnotationWrapper createOverrideAnnotation();


    //verify

    boolean isSameType(TypeMirror t1, TypeMirror t2);

    boolean isSubtype(TypeMirror t1, TypeMirror t2);

    // injection

    ClassWrapper injectImport(CompilationUnitWrapper compilationUnitWrapper, ImportWrapper importWrapper);

    void injectInterface(ClassWrapper classWrapper, TypeElement infType);

    void injectInterface(ClassWrapper classWrapper, TypeElement infType, List<TypeElement> genericTypes);

    void injectMethod(ClassWrapper classWrapper, MethodWrapper methodWrapper);

    // extraction

    Annotation extractAnnotations(TypeMirror type, Class<? extends Annotation> annotation);

    ClassWrapper extractClass(CompilationUnitWrapper compilationUnitWrapper);

    CompilationUnitWrapper extractCompilationUnit(TypeElement typeElement);

    TypeElement extractTypeElement(ClassWrapper classWrapper);

    // convert

    Element asElement(TypeMirror tm);

}
