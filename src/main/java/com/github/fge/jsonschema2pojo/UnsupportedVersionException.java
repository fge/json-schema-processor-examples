package com.github.fge.jsonschema2pojo;

import com.github.fge.jsonschema.exceptions.ProcessingException;

public final class UnsupportedVersionException
    extends ProcessingException
{
    public UnsupportedVersionException()
    {
        super("only draft v3 is supported");
    }
}
