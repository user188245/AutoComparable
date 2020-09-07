package auto.autocomparable;

import auto.util.AnnotationProcessorTool;
import com.sun.source.tree.ExpressionTree;

class PrimitiveCompareMethodImpl extends PrimitiveCompareMethod{

    private AnnotationProcessorTool annotationProcessorTool;

    private ExpressionTree intCompareMethodTree;
    private ExpressionTree longCompareMethodTree;
    private ExpressionTree shortCompareMethodTree;
    private ExpressionTree byteCompareMethodTree;
    private ExpressionTree charCompareMethodTree;
    private ExpressionTree booleanCompareMethodTree;
    private ExpressionTree floatCompareMethodTree;
    private ExpressionTree doubleCompareMethodTree;

    public PrimitiveCompareMethodImpl(AnnotationProcessorTool annotationProcessorTool){
        this.annotationProcessorTool = annotationProcessorTool;
    }

    private ExpressionTree select(String path) {
        return annotationProcessorTool.extractMemberSelect(path);
    }

    @Override
    public ExpressionTree getIntCompareMethodTree() {
        if(intCompareMethodTree == null){
            intCompareMethodTree = select("Integer.compare");
        }
        return intCompareMethodTree;
    }

    @Override
    public ExpressionTree getLongCompareMethodTree() {
        if(longCompareMethodTree == null){
            longCompareMethodTree = select("Long.compare");
        }
        return longCompareMethodTree;
    }

    @Override
    public ExpressionTree getShortCompareMethodTree() {
        if(shortCompareMethodTree == null){
            shortCompareMethodTree = select("Short.compare");
        }
        return shortCompareMethodTree;
    }

    @Override
    public ExpressionTree getByteCompareMethodTree() {
        if(byteCompareMethodTree == null){
            byteCompareMethodTree = select("Byte.compare");
        }
        return byteCompareMethodTree;
    }

    @Override
    public ExpressionTree getCharCompareMethodTree() {
        if(charCompareMethodTree == null){
            charCompareMethodTree = select("Character.compare");
        }
        return charCompareMethodTree;
    }

    @Override
    public ExpressionTree getBooleanCompareMethodTree() {
        if(booleanCompareMethodTree == null){
            booleanCompareMethodTree = select("Boolean.compare");
        }
        return booleanCompareMethodTree;
    }

    @Override
    public ExpressionTree getFloatCompareMethodTree() {
        if(floatCompareMethodTree == null){
            floatCompareMethodTree = select("Float.compare");
        }
        return floatCompareMethodTree;
    }

    @Override
    public ExpressionTree getDoubleCompareMethodTree() {
        if(doubleCompareMethodTree == null){
            doubleCompareMethodTree = select("Double.compare");
        }
        return doubleCompareMethodTree;
    }


}
