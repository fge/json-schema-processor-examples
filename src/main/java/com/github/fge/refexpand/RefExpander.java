package com.github.fge.refexpand;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.LoadingConfiguration;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.processing.Processor;
import com.github.fge.jsonschema.processors.data.SchemaHolder;
import com.github.fge.jsonschema.report.DevNullProcessingReport;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.tree.CanonicalSchemaTree;
import com.github.fge.jsonschema.tree.SchemaTree;
import com.github.fge.jsonschema.util.JacksonUtils;
import com.github.fge.jsonschema.util.JsonLoader;
import com.github.fge.jsonschema.walk.ResolvingSchemaWalker;
import com.github.fge.jsonschema.walk.SchemaListener;
import com.github.fge.jsonschema.walk.SchemaListenerProvider;
import com.github.fge.jsonschema.walk.SchemaWalker;
import com.github.fge.jsonschema.walk.SchemaWalkerProcessor;
import com.github.fge.jsonschema.walk.SchemaWalkerProvider;

import java.io.IOException;

public final class RefExpander
    implements Processor<SchemaHolder, SchemaHolder>
{
    private final SchemaWalkerProcessor<JsonNode> walkerProcessor;

    public RefExpander(final LoadingConfiguration cfg)
    {
        final SchemaWalkerProvider walkerProvider = new SchemaWalkerProvider()
        {
            @Override
            public SchemaWalker newWalker(final SchemaTree tree)
            {
                return new ResolvingSchemaWalker(tree, SchemaVersion.DRAFTV4,
                    cfg);
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

    public static void main(final String... args)
        throws IOException, ProcessingException
    {
        final LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
            .setNamespace("resource:/refexpand/").freeze();
        final RefExpander expander = new RefExpander(cfg);

        final JsonNode node = JsonLoader.fromResource("/refexpand/main.json");
        final SchemaTree tree = new CanonicalSchemaTree(node);
        final SchemaHolder holder = new SchemaHolder(tree);
        final ProcessingReport report = new DevNullProcessingReport();

        final SchemaHolder out = expander.process(report, holder);
        System.out.println(JacksonUtils.prettyPrint(out.getValue()
            .getBaseNode()));
    }
}
