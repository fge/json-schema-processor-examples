package com.github.fge.util;

import com.github.fge.jjschema.JJSchemaFromSource;
import com.github.fge.jjschema.SourceHolder;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.processors.data.SchemaHolder;
import com.github.fge.jsonschema.report.ConsoleProcessingReport;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.util.JacksonUtils;
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
        final JJSchemaFromSource processor = JJSchemaFromSource.getInstance();
        final ProcessingReport report = new ConsoleProcessingReport();
        final SchemaHolder output = processor.process(report, input);

        if (report.isSuccess())
            System.out.println(JacksonUtils.prettyPrint(output.getValue()
                .getBaseNode()));
    }
}
