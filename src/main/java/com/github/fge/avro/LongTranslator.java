package com.github.fge.avro;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.avro.Schema;

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
    void translate(final Schema avroSchema, final MutableTree jsonSchema)
    {
        final ObjectNode node = jsonSchema.getCurrentNode();
        node.put("type", "integer");
        node.put("minimum", Long.MIN_VALUE);
        node.put("maximum", Long.MAX_VALUE);
    }
}
