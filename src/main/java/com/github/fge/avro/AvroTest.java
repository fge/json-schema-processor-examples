package com.github.fge.avro;

import org.apache.avro.Schema;

public final class AvroTest
{
    public static void main(final String... args)
    {
        final String input = "{\"type\":\"string\"}";
        final Schema avroSchema = new Schema.Parser().parse(input);
        System.out.println(avroSchema.getType());
    }
}
