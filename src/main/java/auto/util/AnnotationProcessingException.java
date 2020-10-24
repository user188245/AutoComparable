package auto.util;

/**
 * @author user188245
 */
public class AnnotationProcessingException extends IllegalArgumentException {

    private final int code;

    public AnnotationProcessingException(int code, String message) {
        super(getCodeWithString(code) + " " + message);
        this.code = code;
    }

    public static String getCodeWithString(int code) {
        return "ERROR:" + code + ";";
    }

    public int getCode() {
        return code;
    }

}
