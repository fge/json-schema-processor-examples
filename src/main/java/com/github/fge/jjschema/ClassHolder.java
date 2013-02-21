package com.github.fge.jjschema;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.util.JacksonUtils;
import com.github.fge.jsonschema.util.ValueHolder;

public final class ClassHolder
    extends ValueHolder<Class<?>>
{
    public ClassHolder(final Class<?> value)
    {
        super("class", value);
    }

    @Override
    protected JsonNode valueAsJson()
    {
        return JacksonUtils.nodeFactory().textNode(value.getName());
    }
}
