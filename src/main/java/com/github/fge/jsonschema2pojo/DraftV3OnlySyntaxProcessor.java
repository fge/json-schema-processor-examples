package com.github.fge.jsonschema2pojo;

import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.keyword.syntax.SyntaxProcessor;
import com.github.fge.jsonschema.core.messages.JsonSchemaSyntaxMessageBundle;
import com.github.fge.jsonschema.core.processing.Processor;
import com.github.fge.jsonschema.core.processing.ProcessorMap;
import com.github.fge.jsonschema.core.ref.JsonRef;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.core.tree.SchemaTree;
import com.github.fge.jsonschema.core.util.ValueHolder;
import com.github.fge.jsonschema.library.DraftV3Library;
import com.github.fge.jsonschema.processors.syntax.SyntaxValidator;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import com.google.common.base.Function;

public final class DraftV3OnlySyntaxProcessor
    implements Processor<ValueHolder<SchemaTree>, ValueHolder<SchemaTree>>
{
    private static final Function<ValueHolder<SchemaTree>, JsonRef> FUNCTION
        = new Function<ValueHolder<SchemaTree>, JsonRef>()
    {
        @Override
        public JsonRef apply(final ValueHolder<SchemaTree> input)
        {
            return input.getValue().getDollarSchema();
        }
    };

    private final Processor<ValueHolder<SchemaTree>,  ValueHolder<SchemaTree>>
        processor;

    public DraftV3OnlySyntaxProcessor()
    {
        final MessageBundle bundle
            = MessageBundles.getBundle(JsonSchemaSyntaxMessageBundle.class);
        final SyntaxProcessor syntaxProcessor = new SyntaxProcessor(bundle,
            DraftV3Library.get().getSyntaxCheckers());

        final JsonRef draftv3
            = JsonRef.fromURI(SchemaVersion.DRAFTV3.getLocation());

        final ProcessorMap<JsonRef, ValueHolder<SchemaTree>, ValueHolder<SchemaTree>>
            map
            = new ProcessorMap<JsonRef, ValueHolder<SchemaTree>, ValueHolder<SchemaTree>>(FUNCTION)
                .addEntry(draftv3, syntaxProcessor)
                .addEntry(JsonRef.emptyRef(), syntaxProcessor)
                .setDefaultProcessor(UNSUPPORTED);

        processor = map.getProcessor();
    }

    @Override
    public ValueHolder<SchemaTree> process(final ProcessingReport report,
        final ValueHolder<SchemaTree> input)
        throws ProcessingException
    {
        return processor.process(report, input);
    }

    private static final Processor<ValueHolder<SchemaTree>,
        ValueHolder<SchemaTree>> UNSUPPORTED
        = new Processor<ValueHolder<SchemaTree>, ValueHolder<SchemaTree>>()
    {
        @Override
        public ValueHolder<SchemaTree> process(final ProcessingReport report,
            final ValueHolder<SchemaTree> input)
            throws ProcessingException
        {
            throw new UnsupportedVersionException();
        }
    };
}
