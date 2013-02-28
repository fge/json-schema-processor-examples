package com.github.fge.avro;

import com.github.fge.jsonschema.util.JsonLoader;
import com.github.fge.jsonschema.util.RhinoHelper;
import org.apache.avro.Schema;

import java.io.IOException;

public final class AvroPlayground
{
    public static void main(final String... args)
        throws IOException
    {
        final String input = JsonLoader.fromResource("/t.json").toString();
        final Schema avroSchema = new Schema.Parser().parse(input);
        final Schema.Field field = avroSchema.getField("recordField");
        System.out.println(field.schema().getFullName());
        final String regex = "^[\u0000-\u00ff]*$";
        System.out.println(RhinoHelper.regexIsValid(regex));
        System.out.println(RhinoHelper.regMatch(regex, "hello"));
    }
}
