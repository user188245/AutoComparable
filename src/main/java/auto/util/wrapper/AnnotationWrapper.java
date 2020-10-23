package auto.util.wrapper;

import com.sun.source.tree.AnnotationTree;
import org.eclipse.jdt.internal.compiler.ast.Annotation;

/**
 * @author user188245
 * @param <T> the annotation syntax tree
 */
public class AnnotationWrapper<T> extends ExpressionWrapper<T> {

    protected AnnotationWrapper(T data) {
        super(data);
    }

    public static AnnotationWrapper<AnnotationTree> from(AnnotationTree data){
        return new AnnotationWrapper<AnnotationTree>(data);
    }

    public static AnnotationWrapper<Annotation> from(Annotation data){
        return new AnnotationWrapper<Annotation>(data);
    }
}
