package auto.util;

import com.sun.source.tree.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;

//todo
public interface AnnotationProcessorTool {

    ProcessingEnvironment getProcessingEnvironment();


    // creation

    TypeElement createTypeElement(Class<?> cls);

    ImportTree createImport(TypeElement e);

    TypeMirror createGenericTypeMirror(TypeMirror prototype, List<TypeMirror> genericsTypes);


    //verify

    boolean isSameType(TypeMirror t1, TypeMirror t2);

    boolean isSubtype(TypeMirror t1, TypeMirror t2);

    // injection

    ClassTree injectImport(CompilationUnitTree compilationUnitTree, ImportTree importTree);

    void injectInterface(ClassTree classTree, TypeElement infType);

    void injectInterface(ClassTree classTree, TypeElement infType, List<TypeElement> genericTypes);

    void injectMethod(ClassTree classTree, MethodTree methodTree);

    void injectFieldAccessRight(MemberSelectTree memberSelectTree, String param);

    void injectFieldAccessLeft(MemberSelectTree memberSelectTree, String param);

    // extraction

    ClassTree extractTree(CompilationUnitTree compilationUnit);

    TypeElement extractTypeElement(ClassTree classTree);

    ExpressionTree extractMemberSelect(String fullPath);
}
