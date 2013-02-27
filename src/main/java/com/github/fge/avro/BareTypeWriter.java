package com.github.fge.avro;

import com.fasterxml.jackson.databind.node.ObjectNode;

public final class BareTypeWriter
    implements ObjectNodeWriter
{
    private final String type;

    public BareTypeWriter(final String type)
    {
        this.type = type;
    }

    @Override
    public void writeTo(final ObjectNode jsonSchema)
    {
        jsonSchema.put("type", type);
    }
}
