package com.github.fge.avro;

import com.github.fge.jsonschema.library.Dictionary;
import com.github.fge.jsonschema.library.DictionaryBuilder;

public final class AvroSimpleTypesDictionary
{
    private static final Dictionary<ObjectNodeWriter> DICTIONARY;

    static {
        final DictionaryBuilder<ObjectNodeWriter> builder
            = Dictionary.newBuilder();

        String type;
        ObjectNodeWriter writer;

        /*
         * null
         */
        type = "null";
        writer = new BareTypeWriter(type);
        builder.addEntry(type, writer);

        /*
         * boolean
         */
        type = "boolean";
        writer = new BareTypeWriter(type);
        builder.addEntry(type, writer);

        /*
         * int: a 32bit signed integer
         */
        type = "int";
        writer = IntTypeWriter.getInstance();
        builder.addEntry(type, writer);

        /*
         * long: a 64bit signed integer
         */
        type = "long";
        writer = LongTypeWriter.getInstance();
        builder.addEntry(type, writer);

        /*
         * float: IEEE 754 32bit floating point number
         */
        type = "float";
        writer = new BareTypeWriter("number");
        builder.addEntry(type, writer);

        /*
         * double: IEEE 754 64bit floating point number
         */
        type = "double";
        builder.addEntry(type, writer);

        /*
         * Bytes: in fact, a string
         */
        type = "bytes";
        writer = new BareTypeWriter("string");
        builder.addEntry(type, writer);

        type = "string";
        builder.addEntry(type, writer);

        DICTIONARY = builder.freeze();
    }

    private AvroSimpleTypesDictionary()
    {
    }

    public static Dictionary<ObjectNodeWriter> get()
    {
        return DICTIONARY;
    }
}
