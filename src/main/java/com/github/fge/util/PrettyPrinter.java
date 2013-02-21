package com.github.fge.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.fge.jsonschema.util.JacksonUtils;
import com.google.common.io.Closeables;

import java.io.IOException;
import java.io.StringWriter;

public final class PrettyPrinter
{
    private static final ObjectWriter WRITER = new ObjectMapper()
        .setNodeFactory(JacksonUtils.nodeFactory())
        .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
        .writerWithDefaultPrettyPrinter();

    private PrettyPrinter()
    {
    }

    public static String prettyPrint(final JsonNode node)
        throws IOException
    {
        final StringWriter writer = new StringWriter();
        WRITER.writeValue(writer, node);
        writer.flush();
        Closeables.closeQuietly(writer);
        return writer.toString();
    }
}
