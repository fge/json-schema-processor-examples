package com.github.fge.avro;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.DevNullProcessingReport;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.core.tree.JsonTree;
import com.github.fge.jsonschema.core.tree.SimpleJsonTree;
import com.github.fge.jsonschema.core.util.ValueHolder;
import org.apache.avro.SchemaParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.*;

public final class Avro2JsonSchemaProcessorTest
{
    private JsonNode schema;

    @BeforeClass
    public void load()
        throws IOException
    {
        schema = JsonLoader.fromResource("/illegal.json");
    }

    @Test
    public void illegalSchemasAreReportedAsSuch()
        throws ProcessingException
    {
        final JsonTree tree = new SimpleJsonTree(schema);
        final ValueHolder<JsonTree> input
            = ValueHolder.hold("avroSchema", tree);
        final ProcessingReport report = new DevNullProcessingReport();

        try {
            new Avro2JsonSchemaProcessor().process(report, input);
            fail("No exception thrown!!");
        } catch (IllegalAvroSchemaException e) {
            final ProcessingMessage message = e.getProcessingMessage();
            final JsonNode node = message.asJson();
            assertEquals(node.get("exceptionClass").textValue(),
                SchemaParseException.class.getCanonicalName());
        }
    }
}
