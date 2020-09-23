package auto.util.wrapper;

import com.sun.source.tree.ModifiersTree;

import javax.lang.model.element.Modifier;

public class ModifiersWrapper<T> extends ASTWrapper<T> {

    protected ModifiersWrapper(T data) {
        super(data);
    }

    public static ModifiersWrapper<ModifiersTree> from(ModifiersTree data){
        return new ModifiersWrapper<ModifiersTree>(data);
    }

    public static ModifiersWrapper<Modifier> from(Modifier data){
        return new ModifiersWrapper<Modifier>(data);
    }

}
