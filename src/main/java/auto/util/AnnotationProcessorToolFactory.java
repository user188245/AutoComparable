package auto.util;

import com.sun.tools.javac.processing.JavacProcessingEnvironment;

import javax.annotation.processing.ProcessingEnvironment;

public class AnnotationProcessorToolFactory {

    public static AnnotationProcessorTool instance(JavacProcessingEnvironment processingEnv){
        return new JavacAnnotationProcessorTool(processingEnv);
    }
    public static AnnotationProcessorTool instance(ProcessingEnvironment processingEnv){
        if(processingEnv instanceof JavacProcessingEnvironment){
            return instance((JavacProcessingEnvironment)processingEnv);
        }
        throw new UnsupportedCompilerException();
    }

}
