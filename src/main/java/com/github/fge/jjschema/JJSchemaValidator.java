package com.github.fge.jjschema;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.cfg.LoadingConfiguration;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.library.DraftV4Library;
import com.github.fge.jsonschema.load.SchemaLoader;
import com.github.fge.jsonschema.processors.data.FullData;
import com.github.fge.jsonschema.processors.data.SchemaHolder;
import com.github.fge.jsonschema.processors.ref.RefResolver;
import com.github.fge.jsonschema.processors.validation.ValidationChain;
import com.github.fge.jsonschema.processors.validation.ValidationProcessor;
import com.github.fge.jsonschema.report.ConsoleProcessingReport;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.tree.JsonTree;
import com.github.fge.jsonschema.tree.SimpleJsonTree;

public final class JJSchemaValidator
{
    private final JJSchemaProcessor classToSchema;
    private final ValidationProcessor processor;

    public JJSchemaValidator()
    {
        classToSchema = new JJSchemaProcessor();

        final LoadingConfiguration cfg = LoadingConfiguration.byDefault();
        final SchemaLoader loader = new SchemaLoader(cfg);
        final RefResolver refResolver = new RefResolver(loader);
        final ValidationChain chain = new ValidationChain(refResolver,
            DraftV4Library.get(), true);
        processor = new ValidationProcessor(chain);
    }

    public void validate(final Class<?> c, final JsonNode instance)
        throws ProcessingException
    {
        final ProcessingReport report = new ConsoleProcessingReport();
        final ClassHolder classHolder = new ClassHolder(c);
        final SchemaHolder schemaHolder
            = classToSchema.process(report, classHolder);
        final JsonTree tree = new SimpleJsonTree(instance);
        final FullData fullData = new FullData(schemaHolder.getValue(),
            tree);
        processor.process(report, fullData);
    }
}
