package com.github.fge.jjschema;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.util.ValueHolder;

public final class SourceHolder
    extends ValueHolder<String>
{
    public SourceHolder(final String source)
    {
        super("source", source);
    }

    @Override
    protected JsonNode valueAsJson()
    {
        return FACTORY.textNode(value.length() + " chars");
    }
}
