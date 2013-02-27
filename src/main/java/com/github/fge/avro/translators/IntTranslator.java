package com.github.fge.avro.translators;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.avro.MutableTree;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.report.ProcessingReport;
import org.apache.avro.Schema;

final class IntTranslator
    extends AvroTranslator
{
    private static final AvroTranslator INSTANCE = new IntTranslator();

    private IntTranslator()
    {
    }

    static AvroTranslator getInstance()
    {
        return INSTANCE;
    }

    @Override
    public void translate(final Schema avroSchema, final MutableTree jsonSchema,
        final ProcessingReport report)
        throws ProcessingException
    {
        final ObjectNode node = jsonSchema.getCurrentNode();
        node.put("type", "integer");
        node.put("minimum", Integer.MIN_VALUE);
        node.put("maximum", Integer.MAX_VALUE);
    }
}
