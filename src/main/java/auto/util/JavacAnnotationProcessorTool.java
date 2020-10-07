package auto.util;

import auto.util.wrapper.*;
import com.sun.source.tree.*;
import com.sun.source.util.Trees;
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
import com.sun.tools.javac.util.Pair;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.sun.tools.javac.code.Symbol.ClassSymbol;
import static com.sun.tools.javac.tree.JCTree.*;

class JavacAnnotationProcessorTool extends AbstractAnnotationProcessorTool {

    private Trees trees;
    private TreeMaker treeMaker;
    private Names names;

    JavacAnnotationProcessorTool(JavacProcessingEnvironment processingEnv) {
        super(processingEnv);
        this.trees = Trees.instance(processingEnv);
        Context cxt = processingEnv.getContext();
        this.treeMaker = TreeMaker.instance(cxt);
        this.names = Names.instance(cxt);
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
    public ImportWrapper<ImportTree> createImport(TypeElement e) {
        ClassSymbol classSymbol = (ClassSymbol)e;
        return ImportWrapper.from(treeMaker.Import(treeMaker.QualIdent(classSymbol),false));
    }

    private JCModifiers createModifier(List<AnnotationWrapper> annotations, Set<Modifier> modifiers) {
        com.sun.tools.javac.util.List<JCAnnotation> expr;
        if(annotations == null){
            expr = com.sun.tools.javac.util.List.nil();
        }else{
            List<JCAnnotation> annotationTrees = new LinkedList<JCAnnotation>();
            for(AnnotationWrapper aw : annotations){
                annotationTrees.add((JCAnnotation)aw.getData());
            }
            expr = com.sun.tools.javac.util.List.from(annotationTrees);
        }
        return treeMaker.Modifiers(getModifierFlag(modifiers), expr);
    }

    @Override
    public VariableWrapper<VariableTree> createVariable(VariableElement variableElement, ExpressionWrapper init) {
        return VariableWrapper.from(treeMaker.VarDef((Symbol.VarSymbol)variableElement, init==null?null:(JCExpression)init.getData()));
    }

    @Override
    public MethodInvocationWrapper<MethodInvocationTree> createMethodInvocation(ExpressionWrapper receiver, String selector, List<ExpressionWrapper> args) {
        com.sun.tools.javac.util.List<JCExpression> expr;
        if(args != null){
            List<JCExpression> exprList = new LinkedList<JCExpression>();
            for(ExpressionWrapper ew : args){
                exprList.add((JCExpression) ew.getData());
            }
            expr = com.sun.tools.javac.util.List.from(exprList);
        }else{
            expr = com.sun.tools.javac.util.List.nil();
        }

        JCExpression method = (receiver==null)? (JCExpression)createMemberSelect(selector).getData() :(JCExpression)createMemberSelect(receiver,selector).getData();
        return MethodInvocationWrapper.from(treeMaker.Apply(com.sun.tools.javac.util.List.<JCExpression>nil(), method, expr));
    }

    @Override
    public ReturnWrapper<ReturnTree> createReturn(ExpressionWrapper expr) {
        return ReturnWrapper.from(treeMaker.Return(expr==null?null:(JCExpression) expr.getData()));
    }

    @Override
    public IfWrapper<IfTree> createIf(ExpressionWrapper condExpr, StatementWrapper thenExpr, StatementWrapper elseExpr) {
        return IfWrapper.from(treeMaker.If((JCExpression)condExpr.getData(), thenExpr==null?null:(JCStatement)thenExpr.getData(), elseExpr==null?null:(JCStatement)elseExpr.getData()));
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
    public BinaryWrapper<BinaryTree> createBinaryOperation(ExpressionWrapper leftExpr, ExpressionWrapper rightExpr, BinaryOperator binaryOperator) {
        return BinaryWrapper.from(treeMaker.Binary(toTag(binaryOperator), leftExpr==null?null:(JCExpression)leftExpr.getData(), rightExpr==null?null:(JCExpression)rightExpr.getData()));
    }

    @Override
    public MethodWrapper<MethodTree> createMethod(List<AnnotationWrapper> annotations, Set<Modifier> modifiers, String name, TypeMirror returnType, List<VariableElement> params, List<TypeMirror> thrown, BlockWrapper block, TypeElement from) {
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
        JCTree.JCMethodDecl jcMethodDecl = treeMaker.MethodDef((Symbol.MethodSymbol)method,block==null?null:(JCBlock)block.getData());
        jcMethodDecl.mods = createModifier(annotations, method.getModifiers());
        return MethodWrapper.from(jcMethodDecl);
    }

    @Override
    public BlockWrapper<BlockTree> createBlock(Set<Modifier> modifiers, List<StatementWrapper> statements) {
        if(statements == null){
            throw new IllegalArgumentException();
        }
        List<JCStatement> statementList = new LinkedList<JCStatement>();
        for(StatementWrapper sw : statements){
            statementList.add((JCStatement)sw.getData());
        }

        return BlockWrapper.from(treeMaker.Block(getModifierFlag(modifiers),com.sun.tools.javac.util.List.from(statementList)));
    }

    @Override
    public ExpressionWrapper<ExpressionTree> createMemberSelect(String fullPath) {
        String[] stringSplitAsDelimiter = fullPath.split("\\.");
        JCExpression member = treeMaker.Ident(name(stringSplitAsDelimiter[0]));
        for(int i=1; i<stringSplitAsDelimiter.length; i++){
            member = treeMaker.Select(member,name(stringSplitAsDelimiter[i]));
        }
        return ExpressionWrapper.from(member);
    }

    @Override
    public ExpressionWrapper<ExpressionTree> createMemberSelect(ExpressionWrapper start, String fullPath) {
        JCExpression member = start==null?null:(JCExpression)start.getData();
        for (String s : fullPath.split("\\.")) {
            member = treeMaker.Select(member, name(s));
        }
        return ExpressionWrapper.from(member);
    }

    @Override
    public LiteralWrapper<LiteralTree> createLiteral(Object obj) {
        return LiteralWrapper.from(treeMaker.Literal(obj));
    }

    @Override
    public AnnotationWrapper<AnnotationTree> createOverrideAnnotation() {
        return AnnotationWrapper.from(treeMaker.Annotation(new Attribute.Compound((Type)createTypeElement(Override.class).asType(), com.sun.tools.javac.util.List.<Pair<Symbol.MethodSymbol, Attribute>>nil())));
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
    public ClassWrapper<ClassTree> injectImport(CompilationUnitWrapper compilationUnitWrapper, ImportWrapper importWrapper) {
        JCCompilationUnit jcCompilationUnit = (JCCompilationUnit)compilationUnitWrapper.getData();
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
        defsTmp.addFirst(importWrapper==null?null:(JCTree)importWrapper.getData());
        defsTmp.addFirst(packageDecl);
        defsTmp.addLast(jcClassDecl);
        com.sun.tools.javac.util.List<JCTree> defs = com.sun.tools.javac.util.List.from(defsTmp);
        jcCompilationUnit.defs = null;
        jcCompilationUnit.defs = defs;
        return ClassWrapper.from(jcClassDecl);
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
    public void injectInterface(ClassWrapper classWrapper, TypeElement infType) {
        injectInterface(classWrapper, infType, null);
    }

    @Override
    public void injectInterface(ClassWrapper classWrapper, TypeElement infType, List<TypeElement> genericTypes) {
        injectInterface((JCClassDecl)classWrapper.getData(),infType, genericTypes);
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
    public void injectMethod(ClassWrapper classWrapper, MethodWrapper methodWrapper) {
        JCClassDecl jcClassDecl = (JCClassDecl)classWrapper.getData();
        jcClassDecl.defs = jcClassDecl.defs.append((JCTree)methodWrapper.getData());
    }

    @Override
    public ClassWrapper<ClassTree> extractClass(CompilationUnitWrapper compilationUnitWrapper) {
        List<? extends Tree> list = ((CompilationUnitTree)compilationUnitWrapper.getData()).getTypeDecls();
        Tree tree = list.get(list.size()-1);
        return ClassWrapper.from((ClassTree)tree);
    }

    @Override
    public CompilationUnitWrapper extractCompilationUnit(TypeElement typeElement) {
        return CompilationUnitWrapper.from(trees.getPath(typeElement).getCompilationUnit());
    }

    @Override
    public TypeElement extractTypeElement(ClassWrapper classWrapper) {
        return ((JCClassDecl)classWrapper.getData()).sym;
    }


    private Name name(String s){
        return names.fromString(s);
    }

}
