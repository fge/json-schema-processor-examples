package com.github.fge.avro;

import com.fasterxml.jackson.databind.node.ObjectNode;
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
    void translate(final Schema avroSchema, final MutableTree jsonSchema)
    {
        final ObjectNode node = jsonSchema.getCurrentNode();
        node.put("type", "integer");
        node.put("minimum", Integer.MIN_VALUE);
        node.put("maximum", Integer.MAX_VALUE);
    }
}
