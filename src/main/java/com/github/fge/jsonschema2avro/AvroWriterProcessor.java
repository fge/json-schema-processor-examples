package com.github.fge.jsonschema2avro;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.processing.Processor;
import com.github.fge.jsonschema.processing.ProcessorSelector;
import com.github.fge.jsonschema.processors.data.SchemaHolder;
import com.github.fge.jsonschema.report.ConsoleProcessingReport;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.tree.CanonicalSchemaTree;
import com.github.fge.jsonschema.tree.SchemaTree;
import com.github.fge.jsonschema.util.JsonLoader;
import com.github.fge.jsonschema.util.ValueHolder;
import com.github.fge.jsonschema2avro.writers.ArrayWriter;
import com.github.fge.jsonschema2avro.writers.SimpleTypeWriter;
import org.apache.avro.Schema;

import java.io.IOException;

import static com.github.fge.jsonschema2avro.predicates.AvroPredicates.*;

public final class AvroWriterProcessor
    implements Processor<SchemaHolder, ValueHolder<Schema>>
{
    private final Processor<AvroPayload, ValueHolder<Schema>> processor;

    public AvroWriterProcessor()
    {
        processor = new ProcessorSelector<AvroPayload, ValueHolder<Schema>>()
            .when(simpleType()).then(SimpleTypeWriter.getInstance())
            .when(array()).then(ArrayWriter.getInstance())
            .getProcessor();
    }

    @Override
    public ValueHolder<Schema> process(final ProcessingReport report,
        final SchemaHolder input)
        throws ProcessingException
    {
        final AvroPayload payload = new AvroPayload(input.getValue(), this);
        return processor.process(report, payload);
    }

    public static void main(final String... args)
        throws ProcessingException, IOException
    {
        final JsonNode node
            = JsonLoader.fromString("{\"type\": \"array\"," +
            "\"items\":{\"type\":\"null\"}}");
        final SchemaTree tree = new CanonicalSchemaTree(node);
        final SchemaHolder input = new SchemaHolder(tree);
        final AvroWriterProcessor p = new AvroWriterProcessor();
        final ProcessingReport report = new ConsoleProcessingReport();

        System.out.println(p.process(report, input).getValue());
    }
}
