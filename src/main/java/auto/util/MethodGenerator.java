package auto.util;

import auto.util.wrapper.MethodWrapper;

/**
 * {@code MethodGenerator} can generate a specific method tree.
 * @author user188245
 */
public interface MethodGenerator {

    /**
     * generate a method.
     * @return a complete method tree.
     */
    MethodWrapper generateMethod();

}
