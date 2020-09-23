package auto.util;

public class ASTPositionCorrectorFactory {

    public static ASTPositionCorrector instance(JavacAnnotationProcessorTool apt){
        return new JavacASTPositionCorrector(new JavacASTPositionCorrector.PositionWriter(0));
    }

    public static ASTPositionCorrector instance(AnnotationProcessorTool apt){
        if(apt instanceof JavacAnnotationProcessorTool){
            return instance((JavacAnnotationProcessorTool)apt);
        }
        throw new UnsupportedCompilerException();
    }

}
