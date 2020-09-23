package auto.util;

import auto.util.wrapper.ASTWrapper;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.Pretty;

import java.io.IOException;
import java.io.Writer;

class JavacASTPositionCorrector extends Pretty implements ASTPositionCorrector {

    private final PositionWriter positionWriterMirror;

    static class PositionWriter extends Writer{

        PositionWriter(int pos) {
            this.pos = pos;
        }

        private int pos;

        @Override
        public void write(char[] cbuf, int off, int len){
            pos += Math.min(len, cbuf.length - off);
        }

        @Override
        public void flush(){}

        @Override
        public void close(){}
    }

    JavacASTPositionCorrector(PositionWriter positionWriter){
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
    public void correctPosition(ASTWrapper tree) {
        try {
            printExpr((JCTree)tree.getData());
        } catch (IOException e) { }
    }
}
