package com.github.fge.avro;

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

public final class Avro2JsonSchemaConverter
    implements Processor<ValueHolder<JsonTree>, SchemaHolder>
{
    private static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();

    @Override
    public SchemaHolder process(final ProcessingReport report,
        final ValueHolder<JsonTree> input)
        throws ProcessingException
    {
        final ObjectNode jsonSchema = FACTORY.objectNode();
        return new SchemaHolder(new CanonicalSchemaTree(jsonSchema));
    }
}
