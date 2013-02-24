package com.github.fge.compiler;

import java.io.IOException;
import java.io.Writer;

public final class DevNull
    extends Writer
{
    private static final Writer INSTANCE = new DevNull();

    public static Writer getInstance()
    {
        return INSTANCE;
    }

    @Override
    public void write(final char[] cbuf, final int off, final int len)
        throws IOException
    {
    }

    @Override
    public void flush()
        throws IOException
    {
    }

    @Override
    public void close()
        throws IOException
    {
    }
}
