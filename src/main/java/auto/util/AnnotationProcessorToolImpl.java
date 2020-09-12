package auto.util;

import com.sun.source.tree.*;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.sun.tools.javac.code.Symbol.ClassSymbol;
import static com.sun.tools.javac.tree.JCTree.*;

class AnnotationProcessorToolImpl implements AnnotationProcessorTool {

    private ProcessingEnvironment processingEnv;
    private Elements elements;
    private Types types;
    private TreeMaker treeMaker;
    private Names names;

    AnnotationProcessorToolImpl(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        this.elements = processingEnv.getElementUtils();
        this.types = processingEnv.getTypeUtils();
        if(!(processingEnv instanceof JavacProcessingEnvironment)){
            throw new IllegalArgumentException();
        }
        Context cxt = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(cxt);
        this.names = Names.instance(cxt);
    }

    @Override
    public ProcessingEnvironment getProcessingEnvironment() {
        return this.processingEnv;
    }

    @Override
    public TypeElement createTypeElement(Class<?> cls) {
        return elements.getTypeElement(cls.getCanonicalName());
    }

    @Override
    public TypeMirror createPureType(TypeMirror typeWithGenerics) {
        Type.ClassType type = (Type.ClassType)typeWithGenerics;
        type = type.cloneWithMetadata(type.getMetadata());
        type.typarams_field = com.sun.tools.javac.util.List.nil();
        return type;
    }

    @Override
    public VariableElement createVariableElement(Set<Modifier> modifiers, TypeMirror varType, String varName, TypeElement from) {
        return new Symbol.VarSymbol(getModifierFlag(modifiers), name(varName), (Type)varType, (Symbol)from);
    }

    @Override
    public VariableElement createParameterElement(TypeMirror varType, String varName, TypeElement from) {
        return new Symbol.VarSymbol(Flags.PARAMETER, name(varName), (Type)varType, (Symbol)from);
    }

    @Override
    public ImportTree createImport(TypeElement e) {
        ClassSymbol classSymbol = (ClassSymbol)e;
        return treeMaker.Import(treeMaker.QualIdent(classSymbol),false);
    }

    @Override
    public ModifiersTree createModifier(List<AnnotationTree> annotations, Set<Modifier> modifiers) {
        com.sun.tools.javac.util.List<JCAnnotation> expr;
        if(annotations == null){
            expr = com.sun.tools.javac.util.List.nil();
        }else{
            expr = com.sun.tools.javac.util.List.convert(JCAnnotation.class, com.sun.tools.javac.util.List.from(annotations));
        }
        return treeMaker.Modifiers(getModifierFlag(modifiers), expr);
    }

    @Override
    public VariableTree createVariable(VariableElement variableElement, ExpressionTree init) {
        return treeMaker.VarDef((Symbol.VarSymbol)variableElement, (JCExpression)init);
    }

    @Override
    public MethodInvocationTree createMethodInvocation(ExpressionTree methodExpr, List<ExpressionTree> args) {
        com.sun.tools.javac.util.List<JCExpression> expr;
        if(args != null){
            expr = com.sun.tools.javac.util.List.convert(JCExpression.class, com.sun.tools.javac.util.List.from(args));
        }else{
            expr = com.sun.tools.javac.util.List.nil();
        }
        return treeMaker.Apply(com.sun.tools.javac.util.List.nil(), (JCExpression)methodExpr, expr);
    }

    @Override
    public ReturnTree createReturn(ExpressionTree expr) {
        return treeMaker.Return((JCExpression) expr);
    }

    @Override
    public IfTree createIf(ExpressionTree condExpr, StatementTree thenExpr, StatementTree elseExpr) {
        return treeMaker.If((JCExpression)condExpr, (JCStatement) thenExpr, (JCStatement)elseExpr);
    }

    private Tag toTag(BinaryOperator binaryOperator){
        switch(binaryOperator){
            case EQ:
                return Tag.EQ;
            case GE:
                return Tag.GE;
            case GT:
                return Tag.GT;
            case LE:
                return Tag.LE;
            case LT:
                return Tag.LT;
            case NE:
                return Tag.NE;
            case OR:
                return Tag.OR;
            case SL:
                return Tag.SL;
            case SR:
                return Tag.SR;
            case AND:
                return Tag.AND;
            case DIV:
                return Tag.DIV;
            case MOD:
                return Tag.MOD;
            case MUL:
                return Tag.MUL;
            case USR:
                return Tag.USR;
            case PLUS:
                return Tag.PLUS;
            case BITOR:
                return Tag.BITOR;
            case MINUS:
                return Tag.MINUS;
            case BITAND:
                return Tag.BITAND;
            case BITXOR:
                return Tag.BITXOR;
            default:
                return null;
        }
    }

    @Override
    public BinaryTree createBinaryOperation(ExpressionTree leftExpr, ExpressionTree rightExpr, BinaryOperator binaryOperator) {
        return treeMaker.Binary(toTag(binaryOperator), (JCExpression)leftExpr, (JCExpression)rightExpr);
    }

    @Override
    public TypeMirror createPrimitiveType(TypeKind kind) {
        return types.getPrimitiveType(kind);
    }

    @Override
    public TypeMirror createGenericTypeMirror(TypeMirror prototype, List<TypeMirror> genericTypes) {
        if(genericTypes == null){
            throw new IllegalArgumentException();
        }
        Type.ClassType type = (Type.ClassType) prototype;
        Type.ClassType newType = type.cloneWithMetadata(type.getMetadata());
        newType.typarams_field = com.sun.tools.javac.util.List.convert(Type.class,com.sun.tools.javac.util.List.from(genericTypes));
        newType.allparams_field = newType.typarams_field;
        return newType;
    }

    @Override
    public MethodTree createMethod(List<AnnotationTree> annotations, ExecutableElement method, BlockTree block) {
        JCTree.JCMethodDecl jcMethodDecl = treeMaker.MethodDef((Symbol.MethodSymbol)method,(JCBlock)block);
        jcMethodDecl.mods = (JCModifiers)createModifier(annotations, method.getModifiers());
        return jcMethodDecl;
    }

    @Override
    public ExecutableElement createMethodPrototype(Set<Modifier> modifiers, String name, TypeMirror returnType, List<VariableElement> params, List<TypeMirror> thrown, TypeElement from) {
        List<TypeMirror> paramTypes = new LinkedList<>();
        for(VariableElement ve : params){
            paramTypes.add(ve.asType());
        }
        com.sun.tools.javac.util.List<Type> tParams = com.sun.tools.javac.util.List.convert(Type.class, com.sun.tools.javac.util.List.from(paramTypes));
        com.sun.tools.javac.util.List<Type> tThrown;
        if(thrown != null){
            tThrown = com.sun.tools.javac.util.List.convert(Type.class, com.sun.tools.javac.util.List.from(thrown));
        }else{
            tThrown = com.sun.tools.javac.util.List.nil();
        }
        Type.MethodType methodType = new Type.MethodType(
                tParams,
                (Type)returnType,
                tThrown,
                null
        );
        Symbol.MethodSymbol method = new Symbol.MethodSymbol(
                getModifierFlag(modifiers),
                name(name),
                methodType,
                (ClassSymbol)from);
        method.params = com.sun.tools.javac.util.List.convert(Symbol.VarSymbol.class, com.sun.tools.javac.util.List.from(params));
        return method;
    }

    @Override
    public BlockTree createBlock(Set<Modifier> modifiers, List<StatementTree> statements) {
        if(statements == null){
            throw new IllegalArgumentException();
        }
        return treeMaker.Block(getModifierFlag(modifiers),com.sun.tools.javac.util.List.convert(JCStatement.class, com.sun.tools.javac.util.List.from(statements)));
    }

    @Override
    public ExpressionTree createMemberSelect(String fullPath) {
        String[] stringSplitAsDelimiter = fullPath.split("\\.");
        JCExpression member = treeMaker.Ident(name(stringSplitAsDelimiter[0]));
        for(int i=1; i<stringSplitAsDelimiter.length; i++){
            member = treeMaker.Select(member,name(stringSplitAsDelimiter[i]));
        }
        return member;
    }

    @Override
    public ExpressionTree createMemberSelect(ExpressionTree start, String fullPath) {
        JCExpression member = (JCExpression)start;
        for (String s : fullPath.split("\\.")) {
            member = treeMaker.Select(member, name(s));
        }
        return member;
    }

    @Override
    public AssignmentTree createAssignment(ExpressionTree target, ExpressionTree value) {
        return null;
    }

    @Override
    public LiteralTree createLiteral(Object obj) {
        return treeMaker.Literal(obj);
    }

    @Override
    public AnnotationTree createOverrideAnnotation() {
        return treeMaker.Annotation(new Attribute.Compound((Type)createTypeElement(Override.class).asType(), com.sun.tools.javac.util.List.nil()));
    }


    private long getModifierFlag(Set<Modifier> modifiers){
        long result = 0;
        if(modifiers == null){
            return result;
        }
        for(Modifier m : modifiers){
            switch(m){
                case ABSTRACT:
                    result |= Flags.ABSTRACT;
                    break;
                case DEFAULT:
                    result |= Flags.DEFAULT;
                    break;
                case FINAL:
                    result |= Flags.FINAL;
                    break;
                case NATIVE:
                    result |= Flags.NATIVE;
                    break;
                case PRIVATE:
                    result |= Flags.PRIVATE;
                    break;
                case PROTECTED:
                    result |= Flags.PROTECTED;
                    break;
                case PUBLIC:
                    result |= Flags.PUBLIC;
                    break;
                case STATIC:
                    result |= Flags.STATIC;
                    break;
                case STRICTFP:
                    result |= Flags.STRICTFP;
                    break;
                case SYNCHRONIZED:
                    result |= Flags.SYNCHRONIZED;
                    break;
                case TRANSIENT:
                    result |= Flags.TRANSIENT;
                    break;
                case VOLATILE:
                    result |= Flags.VOLATILE;
                    break;
            }
        }
        return result;
    }

    @Override
    public boolean isSameType(TypeMirror t1, TypeMirror t2) {
        return types.isSameType(t1,t2);
    }

    @Override
    public boolean isSubtype(TypeMirror t1, TypeMirror t2) {
        return types.isSubtype(t1,t2);
    }

    @Override
    public ClassTree injectImport(CompilationUnitTree compilationUnitTree, ImportTree importTree) {
        JCCompilationUnit jcCompilationUnit = (JCCompilationUnit)compilationUnitTree;
        LinkedList<JCTree> defsTmp = new LinkedList<>();

        JCPackageDecl packageDecl = null;
        JCClassDecl jcClassDecl = null;

        for(JCTree jctree : jcCompilationUnit.defs){
            if(jctree instanceof JCPackageDecl){
                packageDecl = (JCPackageDecl)jctree;
            }else if(jctree instanceof JCClassDecl){
                jcClassDecl = (JCClassDecl) jctree;
            }else if(jctree instanceof JCImport){
                defsTmp.addFirst(jctree);
            }else{
                defsTmp.addLast(jctree);
            }
        }
        if(jcClassDecl == null){
            throw new IllegalArgumentException();
        }
        defsTmp.addFirst((JCTree)importTree);
        defsTmp.addFirst(packageDecl);
        defsTmp.addLast(jcClassDecl);
        com.sun.tools.javac.util.List<JCTree> defs = com.sun.tools.javac.util.List.from(defsTmp);
        jcCompilationUnit.defs = null;
        jcCompilationUnit.defs = defs;
        return jcClassDecl;
    }

    private JCExpression createIdentWithGenerics(TypeElement infType, List<TypeElement> genericTypes) {
        JCExpression implementsUnit = createIdent(infType);
        Symbol sym = ((JCIdent)implementsUnit).sym;
        if(!(sym.getTypeParameters().isEmpty())){
            List<JCExpression> list = new ArrayList<>(genericTypes.size());
            for(TypeElement te : genericTypes){
                list.add(createIdent(te));
            }
            implementsUnit = treeMaker.TypeApply(implementsUnit,com.sun.tools.javac.util.List.from(list));
        }
        return implementsUnit;
    }

    @Override
    public void injectInterface(ClassTree classTree, TypeElement infType) {
        injectInterface(classTree,infType, null);
    }

    @Override
    public void injectInterface(ClassTree classTree, TypeElement infType, List<TypeElement> genericTypes) {
        injectInterface((JCClassDecl)classTree,infType, genericTypes);
    }

    private void injectInterface(JCClassDecl jcClassDecl, TypeElement infType, List<TypeElement> genericTypes){
        JCExpression implementsUnit = createIdentWithGenerics(infType,genericTypes);
        com.sun.tools.javac.util.List<JCTree.JCExpression> listImplementing = jcClassDecl.implementing.append(implementsUnit);
        Symbol sym;
        if(implementsUnit instanceof JCIdent){
            sym = ((JCIdent)implementsUnit).sym;
        }else if(implementsUnit instanceof JCTypeApply){
            sym = ((JCIdent)(((JCTypeApply)implementsUnit).clazz)).sym;
        }else{
            throw new IllegalArgumentException();
        }
        jcClassDecl.implementing = null;
        jcClassDecl.implementing = listImplementing;
        com.sun.tools.javac.util.List<Type> listInterfacesField = jcClassDecl.sym.getInterfaces().append(sym.asType());
        ((Type.ClassType)jcClassDecl.sym.type).interfaces_field = null;
        ((Type.ClassType)jcClassDecl.sym.type).interfaces_field = listInterfacesField;
        ((Type.ClassType)jcClassDecl.sym.type).all_interfaces_field = listInterfacesField;
    }

    private JCExpression createIdent(TypeElement te){
        return treeMaker.Ident((Symbol)te);
    }

    @Override
    public void injectMethod(ClassTree classTree, MethodTree methodTree) {
        JCClassDecl jcClassDecl = (JCClassDecl) classTree;
        jcClassDecl.defs = jcClassDecl.defs.append((JCTree) methodTree);
    }

    @Override
    public ClassTree extractTree(CompilationUnitTree compilationUnit) {
        List<? extends Tree> list = compilationUnit.getTypeDecls();
        Tree tree = list.get(list.size()-1);
        return (ClassTree)tree;
    }

    @Override
    public TypeElement extractTypeElement(ClassTree classTree) {
        return ((JCClassDecl)classTree).sym;
    }


    private Name name(String s){
        return names.fromString(s);
    }

}
