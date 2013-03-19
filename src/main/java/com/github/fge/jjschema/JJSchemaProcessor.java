package com.github.fge.jjschema;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.processing.Processor;
import com.github.fge.jsonschema.report.ProcessingMessage;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.tree.CanonicalSchemaTree;
import com.github.fge.jsonschema.tree.SchemaTree;
import com.github.fge.jsonschema.util.ValueHolder;
import com.github.reinert.jjschema.JsonSchemaGenerator;
import com.github.reinert.jjschema.SchemaGeneratorBuilder;

public final class JJSchemaProcessor
    implements Processor<ClassHolder, ValueHolder<SchemaTree>>
{
    private static final JsonSchemaGenerator GENERATOR
        = SchemaGeneratorBuilder.draftV4Schema().build();

    @Override
    public ValueHolder<SchemaTree> process(final ProcessingReport report,
        final ClassHolder input)
        throws ProcessingException
    {
        final ProcessingMessage message = input.newMessage();
        report.debug(message.message("processing"));
        final JsonNode node = GENERATOR.generateSchema(input.getValue());
        final SchemaTree tree = new CanonicalSchemaTree(node);
        return ValueHolder.hold("schema", tree);
    }
}
