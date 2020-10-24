package auto.util.wrapper;

import com.sun.source.tree.AnnotationTree;
import org.eclipse.jdt.internal.compiler.ast.Annotation;

/**
 * @param <T> the annotation syntax tree
 * @author user188245
 */
public class AnnotationWrapper<T> extends ExpressionWrapper<T> {

    protected AnnotationWrapper(T data) {
        super(data);
    }

    public static AnnotationWrapper<AnnotationTree> from(AnnotationTree data) {
        return new AnnotationWrapper<AnnotationTree>(data);
    }

    public static AnnotationWrapper<Annotation> from(Annotation data) {
        return new AnnotationWrapper<Annotation>(data);
    }
}
