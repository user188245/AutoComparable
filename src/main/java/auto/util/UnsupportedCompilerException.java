package auto.util;

public class UnsupportedCompilerException extends RuntimeException {
    public UnsupportedCompilerException() {
        super("The current compiler has not been supported yet.");
        printStackTrace();
    }
}
