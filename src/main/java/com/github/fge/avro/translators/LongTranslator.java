package com.github.fge.avro.translators;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.avro.MutableTree;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.report.ProcessingReport;
import org.apache.avro.Schema;

import static com.github.fge.avro.messages.Avro2JsonSchemaMessages.*;

final class LongTranslator
    extends AvroTranslator
{
    private static final AvroTranslator INSTANCE = new LongTranslator();

    private LongTranslator()
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
        report.warn(newMsg(jsonSchema, BRAINDEAD_LANGUAGES));
        final ObjectNode node = jsonSchema.getCurrentNode();
        node.put("type", "integer");
        node.put("minimum", Long.MIN_VALUE);
        node.put("maximum", Long.MAX_VALUE);
    }
}
