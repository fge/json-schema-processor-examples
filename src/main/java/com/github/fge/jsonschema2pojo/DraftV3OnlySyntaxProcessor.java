package com.github.fge.jsonschema2pojo;

import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.library.DraftV3Library;
import com.github.fge.jsonschema.processing.Processor;
import com.github.fge.jsonschema.processing.ProcessorMap;
import com.github.fge.jsonschema.processors.data.SchemaHolder;
import com.github.fge.jsonschema.processors.syntax.SyntaxProcessor;
import com.github.fge.jsonschema.ref.JsonRef;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.google.common.base.Function;

public final class DraftV3OnlySyntaxProcessor
    implements Processor<SchemaHolder, SchemaHolder>
{
    private final Processor<SchemaHolder, SchemaHolder> processor;

    public DraftV3OnlySyntaxProcessor()
    {
        final SyntaxProcessor syntaxProcessor
            = new SyntaxProcessor(DraftV3Library.get());

        final JsonRef draftv3
            = JsonRef.fromURI(SchemaVersion.DRAFTV3.getLocation());

        final ProcessorMap<JsonRef, SchemaHolder, SchemaHolder> map
            = new SchemaMap()
                .addEntry(draftv3, syntaxProcessor)
                .addEntry(JsonRef.emptyRef(), syntaxProcessor)
                .setDefaultProcessor(UNSUPPORTED);

        processor = map.getProcessor();
    }

    @Override
    public SchemaHolder process(final ProcessingReport report,
        final SchemaHolder input)
        throws ProcessingException
    {
        return processor.process(report, input);
    }

    private static final Processor<SchemaHolder, SchemaHolder> UNSUPPORTED
        = new Processor<SchemaHolder, SchemaHolder>()
    {
        @Override
        public SchemaHolder process(final ProcessingReport report,
            final SchemaHolder input)
            throws ProcessingException
        {
            throw new UnsupportedVersionException();
        }
    };

    private static final class SchemaMap
        extends ProcessorMap<JsonRef, SchemaHolder, SchemaHolder>
    {

        @Override
        protected Function<SchemaHolder, JsonRef> f()
        {
            return new Function<SchemaHolder, JsonRef>()
            {
                @Override
                public JsonRef apply(final SchemaHolder input)
                {
                    return input.getValue().getDollarSchema();
                }
            };
        }
    }
}
