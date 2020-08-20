package autocomparable.annotation.processing;

import autocomparable.ComparableInjector;
import autocomparable.annotation.AutoComparable;
import util.AnnotationProcessorTool;
import util.AnnotationProcessorToolFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.LinkedHashSet;
import java.util.Set;

public class AutoComparableProcessor extends AbstractProcessor {

    private ComparableInjector comparableInjector;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        AnnotationProcessorTool apt = AnnotationProcessorToolFactory.instance(processingEnv);
        this.comparableInjector = new ComparableInjector(apt);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedAnnotationTypes = new LinkedHashSet<>();
        supportedAnnotationTypes.add(AutoComparable.class.getCanonicalName());
        return supportedAnnotationTypes;
    }

    //todo
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }
}
