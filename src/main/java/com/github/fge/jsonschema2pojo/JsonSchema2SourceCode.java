package com.github.fge.jsonschema2pojo;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.exceptions.InvalidSchemaException;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.messages.SyntaxMessages;
import com.github.fge.jsonschema.processing.Processor;
import com.github.fge.jsonschema.processors.data.SchemaHolder;
import com.github.fge.jsonschema.report.ConsoleProcessingReport;
import com.github.fge.jsonschema.report.ProcessingMessage;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.tree.CanonicalSchemaTree;
import com.github.fge.jsonschema.tree.SchemaTree;
import com.github.fge.jsonschema.util.JsonLoader;
import com.github.fge.jsonschema.util.ValueHolder;
import com.github.fge.util.SimpleValueHolder;
import com.google.common.io.Files;
import com.googlecode.jsonschema2pojo.SchemaMapper;
import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.writer.SingleStreamCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public final class JsonSchema2SourceCode
    implements Processor<SchemaHolder, ValueHolder<String>>
{
    private static final Charset UTF8 = Charset.forName("UTF-8");

    private static final String CLASSNAME = "Whatever";
    private static final String PKGNAME = "com.github.fge.compiled";

    private final Processor<SchemaHolder, SchemaHolder> processor
        = new DraftV3OnlySyntaxProcessor();

    @Override
    public ValueHolder<String> process(final ProcessingReport report,
        final SchemaHolder input)
        throws ProcessingException
    {
        /*
         * First check if the syntax is valid
         */
        processor.process(report, input);
        if (!report.isSuccess())
            throw new InvalidSchemaException(new ProcessingMessage()
                .message(SyntaxMessages.INVALID_SCHEMA));

        final JsonNode schema = input.getValue().getBaseNode();
        final JCodeModel model = new JCodeModel();
        final SchemaMapper mapper = new SchemaMapper();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CodeWriter writer = new SingleStreamCodeWriter(out);

        final File file;
        try {
            file = schemaToFile(schema);
        } catch (IOException e) {
            throw new ProcessingException("cannot create file", e);
        }

        try {
            mapper.generate(model, CLASSNAME, PKGNAME, file.toURI().toURL());
            model.build(writer);
            final String code = out.toString("UTF-8");
            return new SimpleValueHolder<String>(code);
        } catch (IOException e) {
            throw new ProcessingException("failed to generate source", e);
        } finally {
            if (!file.delete())
                report.warn(input.newMessage().message("cannot delete file"));
        }
    }

    /*
     * We unfortunately have to do this ;(
     */
    private static File schemaToFile(final JsonNode schema)
        throws IOException
    {
        final File ret = File.createTempFile("schema", ".json");
        final byte[] content = schema.toString().getBytes(UTF8);
        Files.write(content, ret);
        return ret;
    }

    public static void main(final String... args)
        throws IOException, ProcessingException
    {
        final JsonNode schema
            = JsonLoader.fromResource("/jsonschema2pojo/sample1.json");
        final SchemaTree tree = new CanonicalSchemaTree(schema);
        final SchemaHolder input = new SchemaHolder(tree);

        final ProcessingReport report = new ConsoleProcessingReport();
        final Processor<SchemaHolder, ValueHolder<String>> processor
            = new JsonSchema2SourceCode();

        final ValueHolder<String> out = processor.process(report, input);

        System.out.println(out.getValue());
    }
}

