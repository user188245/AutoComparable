package autocomparable;

import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.util.Context;

import javax.annotation.processing.ProcessingEnvironment;

public class ProcessingEnviromentFactory {

    public static ProcessingEnvironment build(){
        Context ctx = new Context();
        return JavacProcessingEnvironment.instance(ctx);
    }

}
