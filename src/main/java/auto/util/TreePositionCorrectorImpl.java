package auto.util;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.Pretty;

import java.io.IOException;
import java.io.Writer;

class TreePositionCorrectorImpl extends Pretty implements TreePositionCorrector {

    private final PositionWriter positionWriterMirror;

    static class PositionWriter extends Writer{

        PositionWriter(int pos) {
            this.pos = pos;
        }

        private int pos;

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            pos += Math.min(len, cbuf.length - off);
        }

        @Override
        public void flush() throws IOException {}

        @Override
        public void close() throws IOException {}
    }

    TreePositionCorrectorImpl(PositionWriter positionWriter){
        super(positionWriter,false);
        positionWriterMirror = positionWriter;
    }

    @Override
    public void printExpr(JCTree tree, int prec) throws IOException {
        tree.setPos(positionWriterMirror.pos);
        super.printExpr(tree, prec);
    }

    @Override
    @SuppressWarnings("all")
    public void correctPosition(Tree tree) {
        try {
            printExpr((JCTree)tree);
        } catch (IOException e) { }
    }
}
