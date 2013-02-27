package com.github.fge.avro;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.fge.jsonschema.util.JacksonUtils;
import org.apache.avro.Schema;

abstract class AvroTranslator
{
    protected static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();

    abstract void translate(final Schema avroSchema,
        final MutableTree jsonSchema);
}
