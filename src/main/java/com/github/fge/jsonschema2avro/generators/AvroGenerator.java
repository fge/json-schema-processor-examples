package com.github.fge.jsonschema2avro.generators;

import org.apache.avro.Schema;

public interface AvroGenerator
{
    Schema generate();
}
