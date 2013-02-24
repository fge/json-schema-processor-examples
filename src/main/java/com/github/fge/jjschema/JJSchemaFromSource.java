package com.github.fge.jjschema;

import com.github.fge.compiler.CompilerOutput;
import com.github.fge.compiler.CompilerProcessor;
import com.github.fge.compiler.CompilingException;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.processing.Processor;
import com.github.fge.jsonschema.processors.data.SchemaHolder;
import com.github.fge.jsonschema.report.ProcessingReport;

import java.io.File;

public final class JJSchemaFromSource
    implements Processor<SourceHolder, SchemaHolder>
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
    }

    @Override
    public SchemaHolder process(final ProcessingReport report,
        final SourceHolder input)
        throws ProcessingException
    {
        final CompilerOutput compilerOutput = compiler.process(report, input);

        try {
            return report.isSuccess()
                ? classToSchema.process(report, extractClass(compilerOutput))
                : null;
        } finally {
            final String dir = compilerOutput.getDirectory().getDirectory();
            rmDashRf(new File(dir));
        }
    }

    private static ClassHolder extractClass(final CompilerOutput compilerOutput)
        throws CompilingException
    {
        final Class<?> c = compilerOutput.getGeneratedClass();
        return new ClassHolder(c);
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
