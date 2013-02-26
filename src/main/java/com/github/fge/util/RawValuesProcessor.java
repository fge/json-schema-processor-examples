package com.github.fge.util;

import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.processing.Processor;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.util.ValueHolder;

public final class RawValuesProcessor<IN, OUT>
{
    private final Processor<ValueHolder<IN>, ValueHolder<OUT>> processor;

    public RawValuesProcessor(
        final Processor<ValueHolder<IN>, ValueHolder<OUT>> processor)
    {
        this.processor = processor;
    }

    OUT processRaw(final ProcessingReport report, final IN input)
        throws ProcessingException
    {
        final ValueHolder<IN> in = new SimpleValueHolder<IN>(input);
        final ValueHolder<OUT> out = processor.process(report, in);
        return out.getValue();
    }
}
