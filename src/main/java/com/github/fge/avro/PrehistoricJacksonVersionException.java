package com.github.fge.avro;

import com.github.fge.jsonschema.exceptions.ProcessingException;

public final class PrehistoricJacksonVersionException
    extends ProcessingException
{
    private static final String ANTIQUATED
        = "Please tell Avro devs to update their Jackson dependency!";

    public PrehistoricJacksonVersionException(final Throwable e)
    {
        super(ANTIQUATED, e);
    }
}
