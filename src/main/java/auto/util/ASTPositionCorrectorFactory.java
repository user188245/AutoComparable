package auto.util;

public class ASTPositionCorrectorFactory {

    public static ASTPositionCorrector instance(JavacAnnotationProcessorTool apt){
        return new JavacASTPositionCorrector(new JavacASTPositionCorrector.PositionWriter(0));
    }
    public static ASTPositionCorrector instance(EclipseAnnotationProcessorTool apt){
        return new EclipseASTPositionCorrector();
    }

    public static ASTPositionCorrector instance(AnnotationProcessorTool apt){
        if(apt instanceof JavacAnnotationProcessorTool){
            return instance((JavacAnnotationProcessorTool)apt);
        }
        if(apt instanceof EclipseAnnotationProcessorTool){
            return instance((EclipseAnnotationProcessorTool) apt);
        }
        throw new UnsupportedCompilerException();
    }

}
