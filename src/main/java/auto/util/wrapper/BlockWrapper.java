package auto.util.wrapper;


import com.sun.source.tree.BlockTree;
import org.eclipse.jdt.internal.compiler.ast.Block;

public class BlockWrapper<T> extends StatementWrapper<T> {

    protected BlockWrapper(T data) {
        super(data);
    }

    public static BlockWrapper<BlockTree> from(BlockTree data){
        return new BlockWrapper<BlockTree>(data);
    }

    public static BlockWrapper<Block> from(Block data){
        return new BlockWrapper<Block>(data);
    }

}
