package com.github.fge.avro;

import com.github.fge.jsonschema.library.Dictionary;
import com.github.fge.jsonschema.library.DictionaryBuilder;

public final class AvroSimpleTypesDictionary
{
    private static final Dictionary<ObjectNodeWriter> DICTIONARY;

    static {
        final DictionaryBuilder<ObjectNodeWriter> builder
            = Dictionary.newBuilder();

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
