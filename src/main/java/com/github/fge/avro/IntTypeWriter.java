package com.github.fge.avro;

import com.fasterxml.jackson.databind.node.ObjectNode;

public final class IntTypeWriter
    implements ObjectNodeWriter
{
    private static final ObjectNodeWriter INSTANCE = new IntTypeWriter();

    private IntTypeWriter()
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
            .put("minimum", Integer.MIN_VALUE)
            .put("maximum", Integer.MAX_VALUE);
    }
}
