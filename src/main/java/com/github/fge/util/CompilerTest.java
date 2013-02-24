package com.github.fge.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.compiler.CompilerOutput;
import com.github.fge.compiler.CompilerOutputDirectory;
import com.github.fge.compiler.CompilerProcessor;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.report.ConsoleProcessingReport;
import com.github.fge.jsonschema.report.LogLevel;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.util.ValueHolder;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public final class CompilerTest
{
    private static final File INPUTFILE = new File("/home/fge/tmp/foo.txt");

    private CompilerTest()
        throws IOException, ProcessingException
    {
    }

    public static void main(final String... args)
        throws IOException, ProcessingException
    {
        final String source
            = Files.toString(INPUTFILE, Charset.forName("UTF-8"));
        final SourceHolder input = new SourceHolder(source);
        final CompilerProcessor processor = new CompilerProcessor();
        final ProcessingReport report
            = new ConsoleProcessingReport(LogLevel.DEBUG, LogLevel.NONE);
        final CompilerOutput output = processor.process(report, input);
        final CompilerOutputDirectory directory = output.getDirectory();
        final Class<?> c = directory.getGeneratedClass();
        System.out.println(c.getCanonicalName());
        final File dir = new File(directory.getDirectory());
        if (!rmDashRf(dir))
            System.err.println("Cannot remove " + dir);


    }

    private static final class SourceHolder
        extends ValueHolder<String>
    {

        private SourceHolder(final String value)
        {
            super("source", value);
        }

        @Override
        protected JsonNode valueAsJson()
        {
            return FACTORY.textNode(value);
        }
    }

    private static boolean rmDashRf(final File file)
    {
        if (file.isDirectory())
            for (final File f: file.listFiles())
                if (!rmDashRf(f))
                    return false;


        return file.delete();
    }
}
