package com.github.fge.avro;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.processing.Processor;
import com.github.fge.jsonschema.processors.data.SchemaHolder;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.tree.CanonicalSchemaTree;
import com.github.fge.jsonschema.tree.JsonTree;
import com.github.fge.jsonschema.util.JacksonUtils;
import com.github.fge.jsonschema.util.ValueHolder;

import java.util.Map;

public final class Avro2JsonSchemaConverter
    implements Processor<ValueHolder<JsonTree>, SchemaHolder>
{
    private static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();
    private static final Map<String, ObjectNodeWriter> SIMPLE_TYPE_WRITERS
        = AvroSimpleTypesDictionary.get().entries();

    /*
     * NOTE NOTE NOTE: the Avro schema is supposed to have been validated when
     * entering this processor!
     */
    @Override
    public SchemaHolder process(final ProcessingReport report,
        final ValueHolder<JsonTree> input)
        throws ProcessingException
    {
        final JsonNode avroSchema = input.getValue().getBaseNode();
        final ObjectNode jsonSchema = FACTORY.objectNode();

        final String type = avroSchema.get("type").textValue();
        final ObjectNodeWriter writer = SIMPLE_TYPE_WRITERS.get(type);
        if (writer != null)
            writer.writeTo(jsonSchema);

        return new SchemaHolder(new CanonicalSchemaTree(jsonSchema));
    }
}
