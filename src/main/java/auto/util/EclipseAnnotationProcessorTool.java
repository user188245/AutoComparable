package auto.util;

import auto.util.wrapper.*;
import org.eclipse.jdt.internal.compiler.apt.dispatch.BaseProcessingEnvImpl;
import org.eclipse.jdt.internal.compiler.apt.model.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.lookup.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author user188245
 */
class EclipseAnnotationProcessorTool extends AbstractAnnotationProcessorTool {

    private final Factory factory;
    private final LookupEnvironment lookupEnv;

    private static final char[] OVERRIDE = {'O','v','e','r','r','i','d','e'};
    private static final char[] java = {'j','a','v','a'};
    private static final char[] lang = {'l','a','n','g'};

    private static final int defaultPos = 0;
    private static final int defaultLineNum = 0;

    EclipseAnnotationProcessorTool(BaseProcessingEnvImpl processingEnv) {
        super(processingEnv);
        this.factory = processingEnv.getFactory();
        this.lookupEnv = processingEnv.getLookupEnvironment();
    }

    @Override
    public VariableElement createVariableElement(Set<Modifier> modifiers, TypeMirror varType, String varName, TypeElement from) {
        char[] name = varName.toCharArray();
        TypeBinding tb = getTypeBinding((TypeMirrorImpl)(varType));
        long modifier = getModifierFlag(modifiers);
        LocalVariableBinding lb = new LocalVariableBinding(name,tb,(int)modifier,false);
        return (VariableElement)factory.newElement(lb);
    }

    @Override
    public VariableElement createParameterElement(TypeMirror varType, String varName, TypeElement from) {
        TypeBinding tb = getTypeBinding((TypeMirrorImpl)(varType));
        LocalVariableBinding lb = new LocalVariableBinding(varName.toCharArray(),tb,0,true);
        return (VariableElement)factory.newElement(lb);
    }

    @Override
    public ImportWrapper createImport(TypeElement e) {
        char[][] token;
        long[] sourcePosition;
        String canonicalName = e.getQualifiedName().toString();
        String[] ss = canonicalName.split("\\.");
        token = new char[ss.length][];
        sourcePosition = new long[ss.length];
        Arrays.fill(sourcePosition,defaultPos);
        for(int i=0; i<ss.length; i++){
            token[i] = ss[i].toCharArray();
            sourcePosition[i] = 0; // put zero values without correction
        }
        return ImportWrapper.from(new ImportReference(token,sourcePosition,false, ASTNode.Bit32));
    }

    @Override
    public VariableWrapper createVariable(VariableElement variableElement, ExpressionWrapper init) {
        VariableElementImpl variableElementImpl = (VariableElementImpl) variableElement;
        VariableBinding variableBinding = (VariableBinding)variableElementImpl._binding;

        LocalDeclaration localDeclaration = new LocalDeclaration(variableBinding.name, defaultPos, defaultPos);
        localDeclaration.initialization = (Expression)init.getData();
        localDeclaration.type = new SingleTypeReference(variableBinding.type.sourceName(),defaultPos);
        return VariableWrapper.from(localDeclaration);
    }

    @Override
    public MethodInvocationWrapper createMethodInvocation(ExpressionWrapper receiver, String selector, List<ExpressionWrapper> args){
        MessageSend ms = new MessageSend();
        Expression[] methodArgs = null;
        if(args != null){
            methodArgs = new Expression[args.size()];
            int i = 0;
            for(ExpressionWrapper ew : args){
                methodArgs[i++] = (Expression) ew.getData();
            }
        }
        ms.receiver = (receiver==null)?ThisReference.implicitThis():(Expression) receiver.getData();
        ms.selector = selector.toCharArray();
        ms.arguments = methodArgs;
        return MethodInvocationWrapper.from(ms);
    }

    @Override
    public ReturnWrapper createReturn(ExpressionWrapper expr) {
        return ReturnWrapper.from(new ReturnStatement(expr==null?null:(Expression)expr.getData(),defaultPos,defaultPos));
    }

    @Override
    public IfWrapper createIf(ExpressionWrapper condExpr, StatementWrapper thenExpr, StatementWrapper elseExpr) {
        return IfWrapper.from(new IfStatement(
                (Expression) condExpr.getData(),
                thenExpr==null?null:(Statement) thenExpr.getData(),
                elseExpr==null?null:(Statement)elseExpr.getData(),
                defaultPos,
                defaultPos)
        );
    }

    @Override
    public BinaryWrapper createBinaryOperation(ExpressionWrapper leftExpr, ExpressionWrapper rightExpr, BinaryOperator binaryOperator) {
        Expression left = (Expression) leftExpr.getData();
        Expression right = (Expression) rightExpr.getData();
        int operator = toBinaryOpcode(binaryOperator);
        switch(binaryOperator){
            case EQ:
            case NE:
                return BinaryWrapper.from(new EqualExpression(left, right, operator));
            case AND:
                return BinaryWrapper.from(new AND_AND_Expression(left, right, operator));
            case OR:
                return BinaryWrapper.from(new OR_OR_Expression(left, right, operator));
            case PLUS:
                if(left instanceof BinaryExpression){
                    return createCombinedBinaryExpressionPlus(left,right);
                }else if(right instanceof BinaryExpression){
                    return createCombinedBinaryExpressionPlus(right,left);
                }
            default:
                return BinaryWrapper.from(new BinaryExpression(left, right, operator));
        }
    }

    private BinaryWrapper createCombinedBinaryExpressionPlus(Expression left, Expression right){
        int arity = 1;
        if(left instanceof CombinedBinaryExpression){
            arity += ((CombinedBinaryExpression) left).arity;
        }
        return BinaryWrapper.from(new CombinedBinaryExpression(left, right, OperatorIds.PLUS, arity));
    }

    @Override
    public MethodWrapper createMethod(List<AnnotationWrapper> annotations, Set<Modifier> modifiers, String name, TypeMirror returnType, List<VariableElement> params, List<TypeMirror> thrown, BlockWrapper block, TypeElement from) {
        int i;
        int modifiersArg = (int)getModifierFlag(modifiers);
        char[] selectorArg = name.toCharArray();
        TypeBinding returnTypeArg = getTypeBinding((TypeMirrorImpl)returnType);
        Block b = (Block) block.getData();

        ReferenceBinding[] thrownArg = null;
        if(thrown != null){
            i = 0;
            thrownArg = new ReferenceBinding[thrown.size()];
            for(TypeMirror tm : thrown){
                thrownArg[i++] = (ReferenceBinding) getTypeBinding((TypeMirrorImpl)(tm));
            }
        }

        Argument[] newArguments = new Argument[params.size()];
        TypeBinding[] newParamTypeBinding = new TypeBinding[params.size()];
        i = 0;
        for(VariableElement ve : params){
            VariableBinding vb = (VariableBinding)((VariableElementImpl) ve)._binding;
            TypeBinding tb = vb.type;
            TypeReference tr = new SingleTypeReference(tb.sourceName(),defaultPos);
            Argument arg = new Argument(vb.name, defaultPos, tr,0);
            newParamTypeBinding[i] = tb;
            newArguments[i++] = arg;
        }

        ReferenceBinding declaringClassArg = (SourceTypeBinding)((TypeElementImpl) from)._binding;

        MethodBinding methodBinding = new MethodBinding(modifiersArg,selectorArg,returnTypeArg,newParamTypeBinding,thrownArg,declaringClassArg);
        TypeReference returnTypeReference = new SingleTypeReference(methodBinding.returnType.sourceName(),defaultPos);

        MethodDeclaration methodDeclaration = new MethodDeclaration(null);
        Annotation[] newAnnotations = new Annotation[annotations.size()];
        i = 0;
        for(AnnotationWrapper a : annotations){
            Annotation annotation = (Annotation) a.getData();
            newAnnotations[i++] = annotation;
            if(Arrays.equals(((SingleTypeReference)annotation.type).token,OVERRIDE)){
                // there's the way to detect the meaning of 'Override', but we do not consider it as 'implementation' but 'method overriding'
//                methodBinding.tagBits |= ExtraCompilerModifiers.AccImplementing;
                methodBinding.modifiers |= ExtraCompilerModifiers.AccOverriding;
            }
        }

        TypeReference[] thrownExceptions = null;
        if(methodBinding.thrownExceptions.length > 0){
            thrownExceptions = new TypeReference[methodBinding.thrownExceptions.length];
            i = 0;
            for(ReferenceBinding rb :methodBinding.thrownExceptions){
                thrownExceptions[i++] = new SingleTypeReference(rb.sourceName, defaultPos);
            }
        }

        methodDeclaration.sourceStart = defaultPos;
        methodDeclaration.sourceEnd = defaultPos;
        methodDeclaration.scope = new MethodScope(null,methodDeclaration,false);
        methodDeclaration.binding = methodBinding;
        methodDeclaration.modifiers = methodBinding.modifiers;
        methodDeclaration.selector = methodBinding.selector;
        methodDeclaration.thrownExceptions = thrownExceptions;
        methodDeclaration.annotations = newAnnotations;
        methodDeclaration.arguments = newArguments;
        methodDeclaration.statements = b.statements;
        methodDeclaration.returnType = returnTypeReference;
        return MethodWrapper.from(methodDeclaration);
    }

    @Override
    public BlockWrapper createBlock(Set<Modifier> modifiers, List<StatementWrapper> statements) {
        Block block = new Block(statements.size());
        Statement[] sts = new Statement[statements.size()];
        int i = 0;
        for(StatementWrapper statementWrapper : statements){
            sts[i++] = (Statement) statementWrapper.getData();
        }
        block.statements = sts;
        return BlockWrapper.from(block);
    }

    @Override
    public ExpressionWrapper createMemberSelect(String fullPath) {
        String[] stringSplitAsDelimiter = fullPath.split("\\.");
        Reference receiver = createSingleNameReference(stringSplitAsDelimiter[0]);
        if(stringSplitAsDelimiter.length == 1){
            return ExpressionWrapper.from(receiver);
        }
        if(receiver instanceof ThisReference){
            for(int i=1; i<stringSplitAsDelimiter.length; i++){
                FieldReference newReceiver = new FieldReference(stringSplitAsDelimiter[i].toCharArray(),defaultPos);
                newReceiver.receiver = receiver;
                receiver = newReceiver;
            }
        }else{
            char[][] tokens = new char[stringSplitAsDelimiter.length][];
            long[] pos = new long[stringSplitAsDelimiter.length];
            for(int i=0; i<tokens.length; i++){
                tokens[i] = stringSplitAsDelimiter[i].toCharArray();
                pos[i] = defaultPos;
            }
            receiver = new QualifiedNameReference(tokens,pos,defaultPos,defaultPos);
        }
        return ExpressionWrapper.from(receiver);
    }

    private Reference createSingleNameReference(String str){
        if(str.equals("this")){
            return new ThisReference(defaultPos, defaultPos);
        }else if(str.equals("super")){
            return new SuperReference(defaultPos, defaultPos);
        }
        return new SingleNameReference(str.toCharArray(), defaultPos);
    }

    @Override
    public ExpressionWrapper createMemberSelect(ExpressionWrapper start, String fullPath) {
        Expression expr = (Expression) start.getData();
        if(expr instanceof Reference){
            return createMemberSelect(String.valueOf(expr.printExpression(0, new StringBuffer()).append('.').append(fullPath)));
        }
        throw new AnnotationProcessingException(ExceptionCode.INTERNAL_ERROR, "The expression of member select tree has illegal type.");
    }

    @Override
    public LiteralWrapper createLiteral(Object obj) {
        if(obj == null){
            return LiteralWrapper.from(new NullLiteral(defaultPos,defaultPos));
        }
        char[] name = obj.toString().toCharArray();
        if(obj instanceof CharSequence){
            return LiteralWrapper.from(new StringLiteral(name,defaultPos,defaultPos,defaultLineNum));
        }
        if(obj instanceof Boolean){
            if((Boolean) obj){
                return LiteralWrapper.from(new TrueLiteral(defaultPos,defaultPos));
            }else{
                return LiteralWrapper.from(new FalseLiteral(defaultPos,defaultPos));
            }
        }
        if(obj instanceof Double){
            return LiteralWrapper.from(new DoubleLiteral(name,defaultPos,defaultPos));
        }
        if(obj instanceof Float){
            return LiteralWrapper.from(new FloatLiteral(name,defaultPos,defaultPos));
        }
        if(obj instanceof Long){
            return LiteralWrapper.from(LongLiteral.buildLongLiteral(name,defaultPos,defaultPos));
        }
        if(obj instanceof Integer || obj instanceof Byte || obj instanceof Character || obj instanceof Short){
            return LiteralWrapper.from(IntLiteral.buildIntLiteral(name,defaultPos,defaultPos));
        }
        throw new AnnotationProcessingException(ExceptionCode.INTERNAL_ERROR, "The Argument must belong to primitive type or String");
    }

    @Override
    public AnnotationWrapper createOverrideAnnotation() {
        Annotation override = new MarkerAnnotation(new SingleTypeReference(OVERRIDE,defaultPos),defaultPos);
        return AnnotationWrapper.from(override);
    }

    @Override
    public ClassWrapper injectImport(CompilationUnitWrapper compilationUnitWrapper, ImportWrapper importWrapper) {
        CompilationUnitDeclaration cud = (CompilationUnitDeclaration)compilationUnitWrapper.getData();
        ImportReference importReference = (ImportReference)importWrapper.getData();
        if(isJavaLangClassToken(importReference.tokens)){
            return extractClass(compilationUnitWrapper);
        }
        Arrays.fill(importReference.sourcePositions, cud.sourceStart);
        if(cud.imports != null){
            ImportReference[] newImports = new ImportReference[cud.imports.length+1];
            System.arraycopy(cud.imports,0,newImports,0,cud.imports.length);
            newImports[newImports.length-1] = importReference;
            cud.imports = null;
            cud.imports = newImports;
        }else{
            cud.imports = new ImportReference[]{importReference};
        }
        return extractClass(compilationUnitWrapper);
    }

    private boolean isJavaLangClassToken(char[][] tokens) {
        return tokens.length == 3 && Arrays.equals(tokens[0],EclipseAnnotationProcessorTool.java) && Arrays.equals(tokens[1],EclipseAnnotationProcessorTool.lang);
    }

    @Override
    public void injectInterface(ClassWrapper classWrapper, TypeElement infType) {
        injectInterface(classWrapper, infType, null);
    }

    @Override
    public void injectInterface(ClassWrapper classWrapper, TypeElement infType, List<TypeElement> genericTypes) {
        TypeDeclaration typeDeclaration = (TypeDeclaration)classWrapper.getData();
        TypeReference inf = createInterface((TypeElementImpl)infType,genericTypes);
        injectInterface(typeDeclaration,inf);
    }

    private void injectInterface(TypeDeclaration td, TypeReference inf){
        if(td.superInterfaces != null){
            TypeReference[] newInterfaces = new TypeReference[td.superInterfaces.length+1];
            System.arraycopy(td.superInterfaces, 0, newInterfaces, 0, td.superInterfaces.length);
            newInterfaces[newInterfaces.length-1] = inf;
            td.superInterfaces = null;
            td.superInterfaces = newInterfaces;
        }else{
            td.superInterfaces = new TypeReference[]{inf};
        }
        SourceTypeBinding sourceTypeBinding = td.binding;
        if(sourceTypeBinding.superInterfaces != null){
            ReferenceBinding[] newInterfaceBindings = new ReferenceBinding[sourceTypeBinding.superInterfaces.length+1];
            System.arraycopy(sourceTypeBinding.superInterfaces, 0, newInterfaceBindings, 0, sourceTypeBinding.superInterfaces.length);
            newInterfaceBindings[newInterfaceBindings.length-1] = (ReferenceBinding) inf.resolvedType;
            sourceTypeBinding.setSuperInterfaces(newInterfaceBindings);
        }else{
            sourceTypeBinding.setSuperInterfaces(new ReferenceBinding[]{(ReferenceBinding) inf.resolvedType});
        }
    }

    private TypeReference createInterface(TypeElementImpl infType, List<TypeElement> genericTypes){
        if(genericTypes != null && !genericTypes.isEmpty()){
            char[] name = getName(infType);
            TypeReference[] typeReferences = new TypeReference[genericTypes.size()];
            TypeBinding[] typeBindings = new TypeBinding[genericTypes.size()];
            int i = 0;
            for(TypeElement te : genericTypes){
                typeReferences[i] = createInterface((TypeElementImpl) te);
                typeBindings[i] = typeReferences[i].resolvedType;
                i++;
            }
            ParameterizedSingleTypeReference pstr = new ParameterizedSingleTypeReference(name,typeReferences, 0, defaultPos);
            pstr.resolvedType = lookupEnv.createParameterizedType((ReferenceBinding)infType._binding,typeBindings,null);
            return pstr;
        }
        return createInterface(infType);
    }

    private TypeReference createInterface(TypeElementImpl infType){
        char[] name = getName(infType);
        SingleTypeReference str = new SingleTypeReference(name,defaultPos);
        str.resolvedType = (TypeBinding)infType._binding;
        return str;
    }

    private SourceTypeBinding getSourceTypeBindingFromTypeElement(TypeElementImpl typeElement){
        return (SourceTypeBinding) typeElement._binding;
    }

    private char[] getName(TypeElement e){
        return e.getQualifiedName().toString().toCharArray();
    }

    private long getModifierFlag(Set<Modifier> modifiers){
        long result = 0;
        if(modifiers == null){
            return result;
        }
        for(Modifier m : modifiers){
            switch(m){
                case ABSTRACT:
                    result |= ClassFileConstants.AccAbstract;
                    break;
                case FINAL:
                    result |= ClassFileConstants.AccFinal;
                    break;
                case NATIVE:
                    result |= ClassFileConstants.AccNative;
                    break;
                case PRIVATE:
                    result |= ClassFileConstants.AccPrivate;
                    break;
                case PROTECTED:
                    result |= ClassFileConstants.AccProtected;
                    break;
                case PUBLIC:
                    result |= ClassFileConstants.AccPublic;
                    break;
                case STATIC:
                    result |= ClassFileConstants.AccStatic;
                    break;
                case STRICTFP:
                    result |= ClassFileConstants.AccStrictfp;
                    break;
                case SYNCHRONIZED:
                    result |= ClassFileConstants.AccSynchronized;
                    break;
                case TRANSIENT:
                    result |= ClassFileConstants.AccTransient;
                    break;
                case VOLATILE:
                    result |= ClassFileConstants.AccVolatile;
                    break;
            }
        }
        return result;
    }

    private int toBinaryOpcode(BinaryOperator binaryOperator){
        switch(binaryOperator){
            case EQ:
                return OperatorIds.EQUAL_EQUAL;
            case GE:
                return OperatorIds.GREATER_EQUAL;
            case GT:
                return OperatorIds.GREATER;
            case LE:
                return OperatorIds.LESS_EQUAL;
            case LT:
                return OperatorIds.LESS;
            case NE:
                return OperatorIds.NOT_EQUAL;
            case OR:
                return OperatorIds.OR_OR;
            case SL:
                return OperatorIds.LEFT_SHIFT;
            case SR:
                return OperatorIds.RIGHT_SHIFT;
            case AND:
                return OperatorIds.AND_AND;
            case DIV:
                return OperatorIds.DIVIDE;
            case MOD:
                return OperatorIds.REMAINDER;
            case MUL:
                return OperatorIds.MULTIPLY;
            case USR:
                return OperatorIds.UNSIGNED_RIGHT_SHIFT;
            case PLUS:
                return OperatorIds.PLUS;
            case BITOR:
                return OperatorIds.OR;
            case MINUS:
                return OperatorIds.MINUS;
            case BITAND:
                return OperatorIds.AND;
            case BITXOR:
                return OperatorIds.XOR;
            default:
                return 0;
        }
    }

    private TypeBinding getTypeBinding(TypeMirrorImpl typeMirror){
        switch(typeMirror.getKind()){
            case SHORT:
                return TypeBinding.SHORT;
            case INT:
                return TypeBinding.INT;
            case BYTE:
                return TypeBinding.BYTE;
            case CHAR:
                return TypeBinding.CHAR;
            case LONG:
                return TypeBinding.LONG;
            case BOOLEAN:
                return TypeBinding.BOOLEAN;
            case FLOAT:
                return TypeBinding.FLOAT;
            case DOUBLE:
                return TypeBinding.DOUBLE;
            case VOID:
                return TypeBinding.VOID;
            case NULL:
                return TypeBinding.NULL;
            case DECLARED:
            case TYPEVAR:
                return (TypeBinding)((ElementImpl)asElement(typeMirror))._binding;
            default:
                throw new AnnotationProcessingException(ExceptionCode.INTERNAL_ERROR, "Can't convert the type into binding.");
        }
    }

    @Override
    public void injectMethod(ClassWrapper classWrapper, MethodWrapper methodWrapper) {
        TypeDeclaration cls = (TypeDeclaration)classWrapper.getData();
        AbstractMethodDeclaration method = (AbstractMethodDeclaration)methodWrapper.getData();
        method.compilationResult = cls.compilationResult;
        method.scope.parent = cls.scope;
        method.sourceStart = cls.sourceStart;
        method.sourceEnd = method.sourceStart;
        if(cls.methods != null){
            AbstractMethodDeclaration[] newMethods = new AbstractMethodDeclaration[cls.methods.length+1];
            System.arraycopy(cls.methods,0,newMethods,0,cls.methods.length);
            newMethods[newMethods.length-1] = method;
            cls.methods = null;
            cls.methods = newMethods;
        }else{
            cls.methods = new AbstractMethodDeclaration[]{method};
        }
        SourceTypeBinding stb = cls.binding;
        MethodBinding[] oldMethodBindings = stb.methods();
        if(oldMethodBindings != null){
            MethodBinding[] newMethodBindings = new MethodBinding[oldMethodBindings.length+1];
            System.arraycopy(oldMethodBindings,0,newMethodBindings,0,oldMethodBindings.length);
            newMethodBindings[newMethodBindings.length-1] = method.binding;
            stb.setMethods(newMethodBindings);
        }else{
            stb.setMethods(new MethodBinding[]{method.binding});
        }
    }

    @Override
    public ClassWrapper extractClass(CompilationUnitWrapper compilationUnitWrapper) {
        TypeDeclaration[] types = ((CompilationUnitDeclaration)compilationUnitWrapper.getData()).types;
        return ClassWrapper.from(types[0]);
    }

    @Override
    public CompilationUnitWrapper extractCompilationUnit(TypeElement typeElement) {
        return CompilationUnitWrapper.from(getSourceTypeBindingFromTypeElement((TypeElementImpl) typeElement).scope.compilationUnitScope().referenceContext);
    }

    @Override
    public TypeElement extractTypeElement(ClassWrapper classWrapper) {
        TypeDeclaration td = (TypeDeclaration)classWrapper.getData();
        return (TypeElement)factory.newElement(td.binding);
    }
}
