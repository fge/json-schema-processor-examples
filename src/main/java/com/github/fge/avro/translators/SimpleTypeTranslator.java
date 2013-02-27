package com.github.fge.avro.translators;

import com.github.fge.avro.MutableTree;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.report.ProcessingReport;
import org.apache.avro.Schema;

final class SimpleTypeTranslator
    extends AvroTranslator
{
    private final String typeName;

    SimpleTypeTranslator(final String typeName)
    {
        this.typeName = typeName;
    }

    @Override
    public void translate(final Schema avroSchema, final MutableTree jsonSchema,
        final ProcessingReport report)
        throws ProcessingException
    {
        jsonSchema.put("type", typeName);
    }
}
