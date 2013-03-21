package com.github.fge.jjschema;

import com.github.fge.compiler.CompilerOutput;
import com.github.fge.compiler.CompilerProcessor;
import com.github.fge.compiler.CompilingException;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.processing.RawProcessor;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.tree.SchemaTree;
import com.github.fge.jsonschema.util.ValueHolder;

import java.io.File;

public final class JJSchemaFromSource
    extends RawProcessor<String, SchemaTree>
{
    private static final JJSchemaFromSource INSTANCE = new JJSchemaFromSource();

    private final CompilerProcessor compiler = new CompilerProcessor();
    private final JJSchemaProcessor classToSchema = new JJSchemaProcessor();

    public static JJSchemaFromSource getInstance()
    {
        return INSTANCE;
    }

    private JJSchemaFromSource()
    {
        super("source", "schema");
    }

    @Override
    protected SchemaTree rawProcess(final ProcessingReport report,
        final String input)
        throws ProcessingException
    {
        final ValueHolder<String> holder = ValueHolder.hold("source", input);
        final CompilerOutput compilerOutput = compiler.process(report, holder);

        try {
            final ValueHolder<Class<?>> input1 = extractClass(compilerOutput);
            return report.isSuccess()
                ? classToSchema.process(report, input1).getValue()
                : null;
        } finally {
            final String dir = compilerOutput.getDirectory().getDirectory();
            rmDashRf(new File(dir));
        }
    }

    private static ValueHolder<Class<?>> extractClass(final CompilerOutput
    compilerOutput)
        throws CompilingException
    {
        final Class<?> c = compilerOutput.getGeneratedClass();
        return ValueHolder.<Class<?>>hold("class", c);
    }

    private static void rmDashRf(final File file)
        throws ProcessingException
    {
        if (file.isDirectory())
            for (final File f: file.listFiles())
                rmDashRf(f);

        if (!file.delete())
            throw new ProcessingException("cannot cleanup directory");
    }
}
