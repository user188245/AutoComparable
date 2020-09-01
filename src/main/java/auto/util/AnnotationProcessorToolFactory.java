package util;

import javax.annotation.processing.ProcessingEnvironment;

public class AnnotationProcessorToolFactory {

    public static AnnotationProcessorTool instance(ProcessingEnvironment processingEnv){
        return new AnnotationProcessorToolImpl(processingEnv);
    }

}
