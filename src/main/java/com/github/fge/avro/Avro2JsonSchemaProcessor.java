package com.github.fge.avro;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.avro.translators.AvroTranslator;
import com.github.fge.avro.translators.AvroTranslators;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.processing.Processor;
import com.github.fge.jsonschema.processors.data.SchemaHolder;
import com.github.fge.jsonschema.report.ProcessingMessage;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.tree.CanonicalSchemaTree;
import com.github.fge.jsonschema.tree.JsonTree;
import com.github.fge.jsonschema.util.ValueHolder;
import org.apache.avro.Schema;

import java.util.Map;

public final class Avro2JsonSchemaProcessor
    implements Processor<ValueHolder<JsonTree>, SchemaHolder>
{
    private static final Map<Schema.Type, AvroTranslator> TRANSLATORS
        = AvroTranslators.get();

    /*
     * NOTE NOTE NOTE: the Avro schema is supposed to have been validated when
     * entering this processor!
     */
    @Override
    public SchemaHolder process(final ProcessingReport report,
        final ValueHolder<JsonTree> input)
        throws ProcessingException
    {
        final JsonNode node = input.getValue().getBaseNode();
        /*
         * FIXME: SchemaParseException is an unchecked exception!
         */
        final Schema avroSchema = new Schema.Parser().parse(node.toString());
        final MutableTree tree = new MutableTree();
        final Schema.Type avroType = avroSchema.getType();
        final AvroTranslator translator = TRANSLATORS.get(avroType);

        if (translator == null)
            throw new UnsupportedAvroSchemaException(new ProcessingMessage()
                .message("unsupported type").put("avroType", avroType));

        translator.translate(avroSchema, tree, report);

        return new SchemaHolder(new CanonicalSchemaTree(tree.getBaseNode()));
    }
}
