package com.github.fge.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.util.ValueHolder;

public final class SimpleValueHolder<T>
    extends ValueHolder<T>
{
    public SimpleValueHolder(final T value)
    {
        super("valueType", value);
    }

    @Override
    protected JsonNode valueAsJson()
    {
        return FACTORY.textNode(value.getClass().getCanonicalName());
    }
}
