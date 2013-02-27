package com.github.fge.avro;

import org.apache.avro.Schema;

final class SimpleTypeTranslator
    extends AvroTranslator
{
    private final String typeName;

    SimpleTypeTranslator(final String typeName)
    {
        this.typeName = typeName;
    }

    @Override
    void translate(final Schema avroSchema, final MutableTree jsonSchema)
    {
        jsonSchema.put("type", typeName);
    }
}
