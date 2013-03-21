package com.github.fge.jjschema;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.processing.RawProcessor;
import com.github.fge.jsonschema.report.ProcessingMessage;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.tree.CanonicalSchemaTree;
import com.github.fge.jsonschema.tree.SchemaTree;
import com.github.reinert.jjschema.JsonSchemaGenerator;
import com.github.reinert.jjschema.SchemaGeneratorBuilder;

public final class JJSchemaProcessor
    extends RawProcessor<Class<?>, SchemaTree>
{
    private static final JsonSchemaGenerator GENERATOR
        = SchemaGeneratorBuilder.draftV4Schema().build();

    public JJSchemaProcessor()
    {
        super("class", "schema");
    }

    @Override
    protected SchemaTree rawProcess(final ProcessingReport report,
        final Class<?> input)
        throws ProcessingException
    {
        final ProcessingMessage message = newMessage(input);
        report.debug(message.message("processing"));
        final JsonNode node = GENERATOR.generateSchema(input);
        return new CanonicalSchemaTree(node);
    }
}
