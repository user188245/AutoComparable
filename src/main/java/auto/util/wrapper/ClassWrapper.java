package auto.util.wrapper;


import com.sun.source.tree.ClassTree;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;

public class ClassWrapper<T> extends StatementWrapper<T> {

    protected ClassWrapper(T data) {
        super(data);
    }

    public static ClassWrapper<ClassTree> from(ClassTree data){
        return new ClassWrapper<ClassTree>(data);
    }

    public static ClassWrapper<TypeDeclaration> from(TypeDeclaration data){
        return new ClassWrapper<TypeDeclaration>(data);
    }

}
