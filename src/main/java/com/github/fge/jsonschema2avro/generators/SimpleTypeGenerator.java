package com.github.fge.jsonschema2avro.generators;

import org.apache.avro.Schema;

public final class SimpleTypeGenerator
    implements AvroGenerator
{
    private final Schema.Type type;

    public SimpleTypeGenerator(final Schema.Type type)
    {
        this.type = type;
    }

    @Override
    public Schema generate()
    {
        return Schema.create(type);
    }
}
