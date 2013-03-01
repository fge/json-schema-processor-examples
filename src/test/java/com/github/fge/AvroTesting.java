package com.github.fge;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.util.JsonLoader;
import org.apache.avro.Schema;

import java.io.IOException;

public final class AvroTesting
{
    public static void main(final String... args)
        throws IOException
    {
        final JsonNode node = JsonLoader.fromResource("/illegal.json");

        new Schema.Parser().parse(node.toString());
    }
}
