package com.github.fge.jsonschema2avro;

import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.processing.Processor;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.tree.SchemaTree;
import com.github.fge.jsonschema.util.ValueHolder;
import org.apache.avro.Schema;

public final class JsonSchema2AvroProcessor
    implements Processor<ValueHolder<SchemaTree>, ValueHolder<Schema>>
{
    @Override
    public ValueHolder<Schema> process(final ProcessingReport report,
        final ValueHolder<SchemaTree> input)
        throws ProcessingException
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
