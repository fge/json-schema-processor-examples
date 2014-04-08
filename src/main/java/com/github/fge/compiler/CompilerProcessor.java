package com.github.fge.compiler;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.processing.Processor;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.core.util.ValueHolder;
import com.google.common.collect.ImmutableList;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CompilerProcessor
    implements Processor<ValueHolder<String>, CompilerOutput>
{
    private static final JavaCompiler COMPILER
        = ToolProvider.getSystemJavaCompiler();

    private static final String CANNOT_FIND_COMPILER
        = "cannot find system compiler (do you have a JDK installed?)";
    private static final String CANNOT_FIND_PACKAGENAME
        = "cannot extract package name from source";
    private static final String CANNOT_FIND_CLASSNAME
        = "cannot extract class name from source";
    private static final String CANNOT_BUILD_URI
        = "cannot build URI from class name";

    private static final Pattern PACKAGE_PATTERN
        = Pattern.compile("^package\\s+(\\w+(\\.\\w+)*);", Pattern.MULTILINE);
    private static final Pattern CLASSNAME_PATTERN
        = Pattern.compile("^public\\s+(?:final\\s+)?class\\s+(\\w+)",
        Pattern.MULTILINE);

    @Override
    public CompilerOutput process(final ProcessingReport report,
        final ValueHolder<String> input)
        throws ProcessingException
    {
        /*
         * Check for the presence of the compiler... We don't want to make this
         * check in a static initializer, since it would make the whole package
         * fail.
         */
        if (COMPILER == null)
            throw new CompilingException(CANNOT_FIND_COMPILER);

        final String source = input.getValue();

        /*
         * Extract package name and class name
         */
        final String packageName = extractPkgName(source);
        if (packageName == null)
            throw new CompilingException(CANNOT_FIND_PACKAGENAME);

        final String className = extractClassName(source);
        if (className == null)
            throw new CompilingException(CANNOT_FIND_CLASSNAME);


        final String fullName = packageName + '.' + className;

        /*
         * Create the JavaFileObject necessary for the compiler to run
         */
        final JavaFileObject fileObject;
        try {
            fileObject = new FromStringFileObject(fullName, source);
        } catch (URISyntaxException e) {
            throw new CompilingException(new ProcessingMessage()
                .setMessage(CANNOT_BUILD_URI).put("className", fullName), e);
        }

        /*
         * Create the compiler output directory and relevant options
         */
        final CompilerOutputDirectory outputDirectory
            = new CompilerOutputDirectory(fullName);

        final CompilerOutput output = new CompilerOutput(outputDirectory);

        doCompile(output, fileObject, report);

        return output;
    }

    private static String extractPkgName(final String source)
    {
        final Matcher m = PACKAGE_PATTERN.matcher(source);
        return m.find() ? m.group(1) : null;
    }

    private static String extractClassName(final String source)
    {
        final Matcher m = CLASSNAME_PATTERN.matcher(source);
        return m.find() ? m.group(1) : null;
    }

    private static void doCompile(final CompilerOutput output,
        final JavaFileObject fileObject, final ProcessingReport report)
        throws ProcessingException
    {
        final String directory = output.getDirectory().getDirectory();
        final List<String> options = ImmutableList.of("-d", directory);
        final DiagnosticsReporting reporting = new DiagnosticsReporting();

        final StandardJavaFileManager manager
            = COMPILER.getStandardFileManager(null, Locale.ENGLISH,
                Charset.forName("UTF-8"));
        final JavaCompiler.CompilationTask task
            = COMPILER.getTask(DevNull.getInstance(), manager, reporting,
            options, null, ImmutableList.of(fileObject));

        task.call();
        report.mergeWith(reporting.getReport());
    }
}
