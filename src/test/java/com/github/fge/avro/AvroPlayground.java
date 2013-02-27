package com.github.fge.avro;

import com.github.fge.jsonschema.util.JsonLoader;
import org.apache.avro.Schema;

import java.io.IOException;

public final class AvroPlayground
{
    public static void main(final String... args)
        throws IOException
    {
        final String input = JsonLoader.fromResource("/t.json").toString();
        final Schema avroSchema = new Schema.Parser().parse(input);
        System.out.println(avroSchema.getType());
        System.out.println(avroSchema.getFullName());
        System.out.println(avroSchema.getElementType().getType());
    }
}
