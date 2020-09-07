package auto.autocomparable;

import com.sun.source.tree.ExpressionTree;

import javax.lang.model.type.TypeMirror;

public abstract class PrimitiveCompareMethod {

    public abstract ExpressionTree getIntCompareMethodTree();

    public abstract ExpressionTree getLongCompareMethodTree();

    public abstract ExpressionTree getShortCompareMethodTree();

    public abstract ExpressionTree getByteCompareMethodTree();

    public abstract ExpressionTree getCharCompareMethodTree();

    public abstract ExpressionTree getBooleanCompareMethodTree();

    public abstract ExpressionTree getFloatCompareMethodTree();

    public abstract ExpressionTree getDoubleCompareMethodTree();

    public ExpressionTree getPrimitiveMethodTree(TypeMirror type){
        switch(type.getKind()){
            case INT:
                return getIntCompareMethodTree();
            case LONG:
                return getLongCompareMethodTree();
            case SHORT:
                return getShortCompareMethodTree();
            case BYTE:
                return getByteCompareMethodTree();
            case CHAR:
                return getCharCompareMethodTree();
            case BOOLEAN:
                return getBooleanCompareMethodTree();
            case FLOAT:
                return getFloatCompareMethodTree();
            case DOUBLE:
                return getDoubleCompareMethodTree();
            default:
                return null;
        }
    }

}
