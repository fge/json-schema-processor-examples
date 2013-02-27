package com.github.fge.avro;

import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.report.ProcessingMessage;

public final class UnsupportedAvroSchemaException
    extends ProcessingException
{
    public UnsupportedAvroSchemaException(final ProcessingMessage message)
    {
        super(message);
    }
}
