package autocomparable.annotation.processing;

import autocomparable.ComparableInjector;
import autocomparable.annotation.AutoComparable;
import com.sun.source.util.Trees;
import util.AnnotationProcessorTool;
import util.AnnotationProcessorToolFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.LinkedHashSet;
import java.util.Set;

public class AutoComparableProcessor extends AbstractProcessor {

    private ComparableInjector comparableInjector;
    private Trees trees;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        AnnotationProcessorTool apt = AnnotationProcessorToolFactory.instance(processingEnv);
        this.trees = Trees.instance(processingEnv);;
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
        for(Element e : roundEnv.getElementsAnnotatedWith(AutoComparable.class)){
            comparableInjector.process(trees.getPath(e).getCompilationUnit());
        }
        return false;
    }
}
