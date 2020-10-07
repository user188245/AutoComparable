package auto.util;

import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import org.eclipse.jdt.internal.compiler.apt.dispatch.BaseProcessingEnvImpl;

import javax.annotation.processing.ProcessingEnvironment;

public class AnnotationProcessorToolFactory {

    public static AnnotationProcessorTool instance(JavacProcessingEnvironment processingEnv){
        return new JavacAnnotationProcessorTool(processingEnv);
    }
    public static AnnotationProcessorTool instance(BaseProcessingEnvImpl processingEnv){
        return new EclipseAnnotationProcessorTool(processingEnv);
    }
    public static AnnotationProcessorTool instance(ProcessingEnvironment processingEnv){
        if(processingEnv instanceof JavacProcessingEnvironment){
            return instance((JavacProcessingEnvironment)processingEnv);
        }
        if(processingEnv instanceof BaseProcessingEnvImpl){
            return instance((BaseProcessingEnvImpl)processingEnv);
        }
        throw new UnsupportedCompilerException();
    }

}
