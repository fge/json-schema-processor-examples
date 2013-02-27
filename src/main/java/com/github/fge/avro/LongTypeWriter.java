package com.github.fge.avro;

import com.fasterxml.jackson.databind.node.ObjectNode;

public final class LongTypeWriter
    implements ObjectNodeWriter
{
    private static final ObjectNodeWriter INSTANCE = new LongTypeWriter();

    private LongTypeWriter()
    {
    }

    public static ObjectNodeWriter getInstance()
    {
        return INSTANCE;
    }

    @Override
    public void writeTo(final ObjectNode jsonSchema)
    {
        jsonSchema.put("type", "integer")
            .put("minimum", Long.MIN_VALUE)
            .put("maximum", Long.MAX_VALUE);
    }
}
