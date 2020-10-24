package auto.autocomparable;

import javax.annotation.processing.Processor;
import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CompilerTester {

    private static String testDefaultPath = "/src/test/java";

    private JavaCompiler compiler;
    private StandardJavaFileManager fileManager;
    private DiagnosticListener2 diagnosticListener;
    private List<Class<? extends Processor>> processors;

    public CompilerTester setProcessors(List<Class<? extends Processor>> processors) {
        this.processors = processors;
        return this;
    }

    private class DiagnosticListener2 implements DiagnosticListener<JavaFileObject> {
        private List<Diagnostic<? extends JavaFileObject>> diagnostics = new LinkedList<Diagnostic<? extends JavaFileObject>>();

        @Override
        public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
            Objects.requireNonNull(diagnostic);
            diagnostics.add(diagnostic);
        }

        List<Diagnostic<? extends JavaFileObject>> popRecentDiagnostic() {
            List<Diagnostic<? extends JavaFileObject>> result = diagnostics;
            this.diagnostics = new LinkedList<Diagnostic<? extends JavaFileObject>>();
            return result;
        }
    }

    public CompilerTester(JavaCompiler compiler) {
        this.compiler = compiler;
        this.diagnosticListener = new DiagnosticListener2();
        this.fileManager = compiler.getStandardFileManager(null, null, null);
        try {
            fileManager.setLocation(StandardLocation.SOURCE_PATH, Collections.singletonList(new File(System.getProperty("user.dir") + testDefaultPath)));
        } catch (IOException e) {

        }
    }

    public List<Diagnostic<? extends JavaFileObject>> doCompiles(List<Class<?>> clsses) throws IOException {
        List<JavaFileObject> objectList = new ArrayList<JavaFileObject>();
        for (Class<?> cls : clsses) {
            JavaFileObject fileObject = fileManager.getJavaFileForInput(StandardLocation.SOURCE_PATH, cls.getCanonicalName(), JavaFileObject.Kind.SOURCE);
            objectList.add(fileObject);
        }
        JavaCompiler.CompilationTask task = compiler.getTask(new PrintWriter(System.out), null, diagnosticListener, null, null, objectList);
        List<Processor> processors = new LinkedList<>();
        for (Class<? extends Processor> p : this.processors) {
            try {
                processors.add(p.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new IllegalArgumentException("Invalid processor type");
            }
        }
        task.setProcessors(processors);
        task.call();
        return diagnosticListener.popRecentDiagnostic();
    }

    ;


}
