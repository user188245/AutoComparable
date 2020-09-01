package util;

import com.sun.source.tree.*;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.sun.tools.javac.code.Symbol.ClassSymbol;
import static com.sun.tools.javac.tree.JCTree.*;

//todo
class AnnotationProcessorToolImpl implements AnnotationProcessorTool {

    private ProcessingEnvironment processingEnv;
    private Elements elements;
    private TreeMaker treeMaker;

    AnnotationProcessorToolImpl(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        this.elements = processingEnv.getElementUtils();
        if(!(processingEnv instanceof JavacProcessingEnvironment)){
            throw new IllegalArgumentException();
        }
        this.treeMaker = TreeMaker.instance(((JavacProcessingEnvironment) processingEnv).getContext());
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
    public ImportTree createImport(TypeElement e) {
        if(!(e instanceof ClassSymbol)){
            throw new IllegalArgumentException();
        }
        ClassSymbol classSymbol = (ClassSymbol)e;
        return treeMaker.Import(treeMaker.QualIdent(classSymbol),false);
    }

    @Override
    public ClassTree injectImport(CompilationUnitTree compilationUnitTree, ImportTree importTree) {
        if(!(compilationUnitTree instanceof JCCompilationUnit)){
            throw new IllegalArgumentException();
        }
        if(!(importTree instanceof JCImport)){
            throw new IllegalArgumentException();
        }
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

    @Override
    public void injectInterface(ClassTree classTree, TypeElement infType, List<TypeElement> genericTypes) {
        if(!(classTree instanceof JCClassDecl)){
            throw new IllegalArgumentException();
        }
        injectInterface((JCClassDecl)classTree,infType,genericTypes);
    }

    private void injectInterface(JCClassDecl jcClassDecl, TypeElement infType, List<TypeElement> genericTypes){
        JCExpression implementsUnit = createIdent(infType);
        Symbol sym = ((JCIdent)implementsUnit).sym;
        if(!(sym.getTypeParameters().isEmpty())){
            List<JCExpression> list = new ArrayList<>(genericTypes.size());
            for(TypeElement te : genericTypes){
                list.add(createIdent(te));
            }
            implementsUnit = treeMaker.TypeApply(implementsUnit,com.sun.tools.javac.util.List.from(list));
        }
        com.sun.tools.javac.util.List<JCTree.JCExpression> listImplementing = jcClassDecl.implementing.append(implementsUnit);

        jcClassDecl.implementing = null;
        jcClassDecl.implementing = listImplementing;
        com.sun.tools.javac.util.List<Type> listInterfacesField = jcClassDecl.sym.getInterfaces().append(sym.asType());
        ((Type.ClassType)jcClassDecl.sym.type).interfaces_field = null;
        ((Type.ClassType)jcClassDecl.sym.type).interfaces_field = listInterfacesField;
        ((Type.ClassType)jcClassDecl.sym.type).all_interfaces_field = listInterfacesField;
    }

    private JCExpression createIdent(TypeElement te){
        if(!(te instanceof ClassSymbol)){
            throw new IllegalArgumentException();
        }
        return treeMaker.Ident((Symbol)te);
    }

    @Override
    public void injectMethod(ClassTree classTree, MethodTree methodTree) {
        if(!(classTree instanceof JCClassDecl)){
            throw new IllegalArgumentException();
        }
        if(!(methodTree instanceof JCMethodDecl)){
            throw new IllegalArgumentException();
        }
        JCClassDecl jcClassDecl = (JCClassDecl) classTree;
        jcClassDecl.defs = jcClassDecl.defs.append((JCTree) methodTree);
    }

    @Override
    public ClassTree extractTree(CompilationUnitTree compilationUnit) {
        List<? extends Tree> list = compilationUnit.getTypeDecls();
        Tree tree = list.get(list.size()-1);
        if(!(tree instanceof ClassTree)){
            throw new IllegalArgumentException();
        }
        return (ClassTree)tree;
    }
}
