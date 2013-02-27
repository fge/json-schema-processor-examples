package com.github.fge.avro.translators;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.avro.MutableTree;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.jsonpointer.JsonPointer;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.util.NodeType;
import org.apache.avro.Schema;

public final class RecordTranslator
    extends NamedAvroTypeTranslator
{
    private static final AvroTranslator INSTANCE = new RecordTranslator();

    private RecordTranslator()
    {
        super(Schema.Type.RECORD);
    }

    public static AvroTranslator getInstance()
    {
        return INSTANCE;
    }

    @Override
    protected void doTranslate(final Schema avroSchema,
        final MutableTree jsonSchema, final ProcessingReport report)
        throws ProcessingException
    {
        final JsonPointer pwd = jsonSchema.getPointer();

        jsonSchema.setType(NodeType.OBJECT);
        jsonSchema.getCurrentNode().put("additionalProperties", false);

        final ObjectNode properties = FACTORY.objectNode();
        jsonSchema.getCurrentNode().put("properties", properties);

        final ArrayNode required = FACTORY.arrayNode();
        jsonSchema.getCurrentNode().put("required", required);

        String fieldName;
        Schema fieldSchema;
        Schema.Type fieldType;
        AvroTranslator translator;
        JsonPointer ptr;

        for (final Schema.Field field: avroSchema.getFields()) {
            fieldName = field.name();
            fieldSchema = field.schema();
            fieldType = fieldSchema.getType();
            translator = AvroTranslators.getTranslator(fieldType);
            required.add(fieldName);
            ptr = JsonPointer.of("properties", fieldName);
            properties.put(fieldName, FACTORY.objectNode());
            jsonSchema.setPointer(pwd.append(ptr));
            translator.translate(fieldSchema, jsonSchema, report);
            jsonSchema.setPointer(pwd);
        }
    }
}
