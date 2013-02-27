package com.github.fge.avro.translators;

import com.google.common.collect.ImmutableMap;
import org.apache.avro.Schema;

import java.util.Map;

public final class AvroTranslators
{
    private static final Map<Schema.Type, AvroTranslator> TRANSLATORS;

    private AvroTranslators()
    {
    }

    static {
        final ImmutableMap.Builder<Schema.Type, AvroTranslator> builder
            = ImmutableMap.builder();

        Schema.Type avroType;
        AvroTranslator translator;

        avroType = Schema.Type.NULL;
        translator = new SimpleTypeTranslator("null");
        builder.put(avroType, translator);

        avroType = Schema.Type.BOOLEAN;
        translator = new SimpleTypeTranslator("boolean");
        builder.put(avroType, translator);

        avroType = Schema.Type.STRING;
        translator = new SimpleTypeTranslator("string");
        builder.put(avroType, translator);

        // Reuse for "bytes"
        avroType = Schema.Type.BYTES;
        translator = ByteTranslator.getInstance();
        builder.put(avroType, translator);

        avroType = Schema.Type.INT;
        translator = IntTranslator.getInstance();
        builder.put(avroType, translator);

        avroType = Schema.Type.LONG;
        translator = LongTranslator.getInstance();
        builder.put(avroType, translator);

        avroType = Schema.Type.FLOAT;
        translator = new SimpleTypeTranslator("number");
        builder.put(avroType, translator);

        // Reuse for "double"
        avroType = Schema.Type.DOUBLE;
        builder.put(avroType, translator);

        TRANSLATORS = builder.build();
    }

    public static Map<Schema.Type, AvroTranslator> get()
    {
        return TRANSLATORS;
    }
}
