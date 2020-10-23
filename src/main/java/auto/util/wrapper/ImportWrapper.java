package auto.util.wrapper;

import com.sun.source.tree.ImportTree;
import org.eclipse.jdt.internal.compiler.ast.ImportReference;

/**
 * @author user188245
 * @param <T> the import-call syntax tree
 */
public class ImportWrapper<T> extends ASTWrapper<T> {

    protected ImportWrapper(T data) {
        super(data);
    }

    public static ImportWrapper<ImportTree> from(ImportTree data){
        return new ImportWrapper<ImportTree>(data);
    }

    public static ImportWrapper<ImportReference> from(ImportReference data){
        return new ImportWrapper<ImportReference>(data);
    }
}
