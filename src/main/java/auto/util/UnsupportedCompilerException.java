package auto.util;

/**
 * @author user188245
 */
public class UnsupportedCompilerException extends RuntimeException {
    public UnsupportedCompilerException() {
        super("The current compiler has not been supported yet.");
    }
}
