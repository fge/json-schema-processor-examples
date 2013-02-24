package com.github.fge.compiler;

import com.github.fge.jsonschema.report.MessageProvider;
import com.github.fge.jsonschema.report.ProcessingMessage;

public final class CompilerOutput
    implements MessageProvider
{
    private final CompilerOutputDirectory directory;

    public CompilerOutput(final CompilerOutputDirectory directory)
    {
        this.directory = directory;
    }

    public Class<?> getGeneratedClass()
        throws CompilingException
    {
        return directory.getGeneratedClass();
    }

    public CompilerOutputDirectory getDirectory()
    {
        return directory;
    }

    @Override
    public ProcessingMessage newMessage()
    {
        return new ProcessingMessage();
    }
}
