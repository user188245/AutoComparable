package auto.autocomparable.annotation.processing;

import auto.autocomparable.ComparableInjector;
import auto.autocomparable.annotation.AutoComparable;
import auto.util.AnnotationProcessorTool;
import auto.util.AnnotationProcessorToolFactory;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.LinkedHashSet;
import java.util.Set;

@SupportedSourceVersion(
    SourceVersion.RELEASE_7
)
public class AutoComparableProcessor extends AbstractProcessor {

    private ComparableInjector comparableInjector;
    private AnnotationProcessorTool apt;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.apt = AnnotationProcessorToolFactory.instance(processingEnv);
        this.messager = processingEnv.getMessager();
        this.comparableInjector = new ComparableInjector(this.apt);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedAnnotationTypes = new LinkedHashSet<>();
        supportedAnnotationTypes.add(AutoComparable.class.getCanonicalName());
        return supportedAnnotationTypes;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for(Element e : roundEnv.getElementsAnnotatedWith(AutoComparable.class)){
            try{
                comparableInjector.process(apt.extractCompilationUnit((TypeElement)e));
            }catch(Exception err){
                messager.printMessage(Diagnostic.Kind.ERROR,"Error Occurred on " + e.getSimpleName()  +" during compile.   Err : " + err.getMessage());
                err.printStackTrace();
            }
        }
        return false;
    }
}
