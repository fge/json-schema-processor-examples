package com.github.fge.compiler;

import com.github.fge.jsonschema.core.report.ListProcessingReport;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.google.common.collect.Maps;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import java.util.EnumMap;
import java.util.Locale;

public final class DiagnosticsReporting
    implements DiagnosticListener<JavaFileObject>
{
    private static final EnumMap<Diagnostic.Kind, LogLevel> LEVEL_MAP
        = Maps.newEnumMap(Diagnostic.Kind.class);

    static  {
        LEVEL_MAP.put(Diagnostic.Kind.ERROR, LogLevel.ERROR);
        LEVEL_MAP.put(Diagnostic.Kind.MANDATORY_WARNING, LogLevel.WARNING);
        LEVEL_MAP.put(Diagnostic.Kind.WARNING, LogLevel.WARNING);
        LEVEL_MAP.put(Diagnostic.Kind.NOTE, LogLevel.INFO);
        LEVEL_MAP.put(Diagnostic.Kind.OTHER, LogLevel.DEBUG);
    }

    private final ListProcessingReport report
        = new ListProcessingReport(LogLevel.DEBUG, LogLevel.NONE);

    @Override
    public void report(final Diagnostic<? extends JavaFileObject> diagnostic)
    {
        final LogLevel level = LEVEL_MAP.get(diagnostic.getKind());
        final ProcessingMessage message = new ProcessingMessage()
            .setMessage(diagnostic.getMessage(Locale.ENGLISH))
            .setLogLevel(level)
            .put("line", diagnostic.getLineNumber())
            .put("column", diagnostic.getColumnNumber());

        report.log(level, message);
    }

    public ProcessingReport getReport()
    {
        return report;
    }
}
