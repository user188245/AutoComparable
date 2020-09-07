package auto.autocomparable.annotation.processing;

import auto.autocomparable.ComparableInjector;
import auto.autocomparable.annotation.AutoComparable;
import com.sun.source.util.Trees;
import auto.util.AnnotationProcessorTool;
import auto.util.AnnotationProcessorToolFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.LinkedHashSet;
import java.util.Set;

public class AutoComparableProcessor extends AbstractProcessor {

    private ComparableInjector comparableInjector;
    private Trees trees;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        AnnotationProcessorTool apt = AnnotationProcessorToolFactory.instance(processingEnv);
        this.trees = Trees.instance(processingEnv);
        this.messager = processingEnv.getMessager();
        this.comparableInjector = new ComparableInjector(apt);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedAnnotationTypes = new LinkedHashSet<>();
        supportedAnnotationTypes.add(AutoComparable.class.getCanonicalName());
        return supportedAnnotationTypes;
    }

    //todo
    // if necessary, build hierarchical dependency tree in order to compile the class with @AutoComparable depended by other @AutoComparable class first.
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for(Element e : roundEnv.getElementsAnnotatedWith(AutoComparable.class)){
            try{
                comparableInjector.process(trees.getPath(e).getCompilationUnit());
            }catch(Exception err){
                messager.printMessage(Diagnostic.Kind.ERROR,"Error Occurred on " + e.getSimpleName()  +" during compile.   Err : " + err.getMessage());
                err.printStackTrace();
            }
        }
        return false;
    }
}
