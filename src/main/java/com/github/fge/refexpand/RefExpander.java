package com.github.fge.refexpand;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.processing.Processor;
import com.github.fge.jsonschema.processors.data.SchemaHolder;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.tree.CanonicalSchemaTree;
import com.github.fge.jsonschema.tree.SchemaTree;
import com.github.fge.jsonschema.walk.ResolvingSchemaWalker;
import com.github.fge.jsonschema.walk.SchemaListener;
import com.github.fge.jsonschema.walk.SchemaListenerProvider;
import com.github.fge.jsonschema.walk.SchemaWalker;
import com.github.fge.jsonschema.walk.SchemaWalkerProcessor;
import com.github.fge.jsonschema.walk.SchemaWalkerProvider;

public final class RefExpander
    implements Processor<SchemaHolder, SchemaHolder>
{
    private final SchemaWalkerProcessor<JsonNode> walkerProcessor;

    public RefExpander()
    {
        final SchemaWalkerProvider walkerProvider = new SchemaWalkerProvider()
        {
            @Override
            public SchemaWalker newWalker(final SchemaTree tree)
            {
                return new ResolvingSchemaWalker(tree, SchemaVersion.DRAFTV4);
            }
        };

        final SchemaListenerProvider<JsonNode> listenerProvider
            = new SchemaListenerProvider<JsonNode>()
        {
            @Override
            public SchemaListener<JsonNode> newListener()
            {
                return new SchemaExpander();
            }
        };

        walkerProcessor = new SchemaWalkerProcessor<JsonNode>(walkerProvider,
            listenerProvider);
    }

    @Override
    public SchemaHolder process(final ProcessingReport report,
        final SchemaHolder input)
        throws ProcessingException
    {
        final JsonNode node = walkerProcessor.process(report, input).getValue();
        final SchemaTree tree = new CanonicalSchemaTree(node);
        return new SchemaHolder(tree);
    }
}
