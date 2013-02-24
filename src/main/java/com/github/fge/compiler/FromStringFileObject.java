package com.github.fge.compiler;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public final class FromStringFileObject
    extends SimpleJavaFileObject
{
    private final String sourceCode;

    public FromStringFileObject(final String fullClassName,
        final String sourceCode)
        throws URISyntaxException
    {
        super(buildURI(fullClassName), Kind.SOURCE);
        this.sourceCode = sourceCode;
    }

    @Override
    public CharSequence getCharContent(final boolean ignoreEncodingErrors)
        throws IOException
    {
        return sourceCode;
    }

    private static URI buildURI(final String fullClassName)
        throws URISyntaxException
    {
        final String path = '/' + fullClassName.replace('.', '/')
            + Kind.SOURCE.extension;
        return new URI("string", null, path, null);
    }
}