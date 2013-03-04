package com.github.fge.jsonschema2avro.generators;

import com.google.common.collect.ImmutableMap;
import org.apache.avro.Schema;

import java.util.Map;

public final class AvroGenerators
{
    private static final Map<Schema.Type, AvroGenerator> GENERATORS;

    private AvroGenerators()
    {
    }

    static {
        final ImmutableMap.Builder<Schema.Type, AvroGenerator> builder
            = ImmutableMap.builder();

        Schema.Type type;
        AvroGenerator generator;

        type = Schema.Type.STRING;
        generator = new SimpleTypeGenerator(type);
        builder.put(type, generator);

        GENERATORS = builder.build();
    }

    public static AvroGenerator get(final Schema.Type type)
    {
        return GENERATORS.get(type);
    }
}
