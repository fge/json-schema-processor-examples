package com.github.fge.jjschema;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.processing.Processor;
import com.github.fge.jsonschema.core.report.ConsoleProcessingReport;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.core.tree.JsonTree;
import com.github.fge.jsonschema.core.tree.SchemaTree;
import com.github.fge.jsonschema.core.tree.SimpleJsonTree;
import com.github.fge.jsonschema.core.util.ValueHolder;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.processors.data.FullData;

public final class JJSchemaValidator
{
    private final JJSchemaProcessor classToSchema;
    private final Processor<FullData, FullData> processor;

    public JJSchemaValidator()
    {
        classToSchema = new JJSchemaProcessor();
        processor = JsonSchemaFactory.byDefault().getProcessor();
    }

    public void validate(final Class<?> c, final JsonNode instance)
        throws ProcessingException
    {
        final ProcessingReport report = new ConsoleProcessingReport();
        final ValueHolder<Class<?>> holder
            = ValueHolder.<Class<?>>hold("class", c);
        final ValueHolder<SchemaTree> schemaHolder
            = classToSchema.process(report, holder);
        final JsonTree tree = new SimpleJsonTree(instance);
        final FullData fullData = new FullData(schemaHolder.getValue(),
            tree);
        processor.process(report, fullData);
    }
}
