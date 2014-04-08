package com.github.fge.compiler;


import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;

public final class CompilingException
    extends ProcessingException
{
    public CompilingException(final ProcessingMessage message)
    {
        super(message);
    }

    public CompilingException(final String message)
    {
        super(message);
    }

    public CompilingException(final ProcessingMessage message,
        final Throwable e)
    {
        super(message, e);
    }
}
