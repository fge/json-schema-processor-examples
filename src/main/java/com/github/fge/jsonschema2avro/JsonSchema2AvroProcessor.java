package com.github.fge.jsonschema2avro;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.processing.Processor;
import com.github.fge.jsonschema.report.ConsoleProcessingReport;
import com.github.fge.jsonschema.report.LogLevel;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.tree.CanonicalSchemaTree;
import com.github.fge.jsonschema.tree.SchemaTree;
import com.github.fge.jsonschema.util.JsonLoader;
import com.github.fge.jsonschema.util.ValueHolder;
import com.github.fge.jsonschema.walk.SchemaListener;
import com.github.fge.jsonschema.walk.SchemaListenerProvider;
import com.github.fge.jsonschema.walk.SchemaWalker;
import com.github.fge.jsonschema.walk.SchemaWalkerProvider;
import com.github.fge.jsonschema.walk.SimpleSchemaWalker;
import org.apache.avro.Schema;

import java.io.IOException;

public final class JsonSchema2AvroProcessor
    implements Processor<ValueHolder<SchemaTree>, ValueHolder<Schema>>
{
    private final SchemaListenerProvider<Schema> listenerProvider;
    private final SchemaWalkerProvider walkerProvider;

    public JsonSchema2AvroProcessor()
    {
        listenerProvider = new SchemaListenerProvider<Schema>()
        {
            @Override
            public SchemaListener<Schema> newListener()
            {
                return new AvroSchemaGeneratorListener();
            }
        };

        walkerProvider = new SchemaWalkerProvider()
        {
            @Override
            public SchemaWalker newWalker(final SchemaTree tree)
            {
                return new SimpleSchemaWalker(tree, SchemaVersion.DRAFTV4);
            }
        };
    }
    @Override
    public ValueHolder<Schema> process(final ProcessingReport report,
        final ValueHolder<SchemaTree> input)
        throws ProcessingException
    {
        final SchemaWalker walker = walkerProvider.newWalker(input.getValue());
        final SchemaListener<Schema> listener = listenerProvider.newListener();
        walker.walk(listener, report);
        return ValueHolder.hold(listener.getValue());
    }

    public static void main(final String... args)
        throws IOException, ProcessingException
    {
        final String in = "{\"type\":\"string\"}";
        final JsonNode node = JsonLoader.fromString(in);

        final JsonSchema2AvroProcessor processor
            = new JsonSchema2AvroProcessor();

        final SchemaTree tree = new CanonicalSchemaTree(node);
        final ValueHolder<SchemaTree> input = ValueHolder.hold(tree);
        final ProcessingReport report
            = new ConsoleProcessingReport(LogLevel.DEBUG, LogLevel.FATAL);

        System.out.println(processor.process(report, input).getValue());
    }
}
