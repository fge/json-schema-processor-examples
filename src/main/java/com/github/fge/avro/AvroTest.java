package com.github.fge.avro;

import com.github.fge.jsonschema.util.RhinoHelper;
import org.apache.avro.Schema;

public final class AvroTest
{
    public static void main(final String... args)
    {
        final String input = "{\"type\":\"string\"}";
        final Schema avroSchema = new Schema.Parser().parse(input);
        System.out.println(avroSchema.getType());
        final String regex = "^[\\u0000-\\u00ff]*$";
        System.out.println(RhinoHelper.regexIsValid(regex));
        System.out.println(RhinoHelper.regMatch(regex, "ab"));
    }
}
