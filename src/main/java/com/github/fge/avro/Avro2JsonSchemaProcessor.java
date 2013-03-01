package com.github.fge.avro;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.avro.translators.AvroTranslators;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.processing.Processor;
import com.github.fge.jsonschema.processors.data.SchemaHolder;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.tree.CanonicalSchemaTree;
import com.github.fge.jsonschema.tree.JsonTree;
import com.github.fge.jsonschema.util.ValueHolder;
import org.apache.avro.Schema;
import org.apache.avro.SchemaParseException;

public final class Avro2JsonSchemaProcessor
    implements Processor<ValueHolder<JsonTree>, SchemaHolder>
{
    /*
     * Avro has two very disturbing characteristics:
     *
     * - it uses Jackson 1.8.x -- in 2013??
     * - SchemaParseException is unchecked -- urgh.
     *
     * We have to catch NoSuchMethodError because of the first point, and
     * work around the second. Which can trigger NoSuchMethodError because of
     * the first point...
     */
    @Override
    public SchemaHolder process(final ProcessingReport report,
        final ValueHolder<JsonTree> input)
        throws ProcessingException
    {
        final JsonNode node = input.getValue().getBaseNode();

        final Schema avroSchema;
        try {
            final String s = node.toString();
            avroSchema = new Schema.Parser().parse(s);
        } catch (SchemaParseException e) {
            throw new IllegalAvroSchemaException(e);
        } catch (NoSuchMethodError e) {
            throw new PrehistoricJacksonVersionException(e);
        }

        final MutableTree tree = new MutableTree();
        try {
            final Schema.Type avroType = avroSchema.getType();
            AvroTranslators.getTranslator(avroType)
                .translate(avroSchema, tree, report);
        } catch (NoSuchMethodError e) {
            throw new PrehistoricJacksonVersionException(e);
        }

        return new SchemaHolder(new CanonicalSchemaTree(tree.getBaseNode()));
    }
}
