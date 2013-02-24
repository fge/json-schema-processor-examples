package com.github.fge.compiler;

import com.github.fge.jsonschema.report.ProcessingMessage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.UUID;

public final class CompilerOutput
{
    private static final String CANNOT_CREATE_DIRECTORY
        = "cannot create base directory";
    private static final String CANNOT_CREATE_URI
        = "cannot create URI from base directory";
    private static final String CANNOT_FIND_CLASS
        = "cannot find class even though compilation succeeded??";

    private final File baseDir;
    private final URLClassLoader classLoader;
    private final String fullClassName;

    public CompilerOutput(final String fullClassName)
        throws CompilingException
    {
        this.fullClassName = fullClassName;
        // Note: Unix is supposed here
        final String tmpdir = System.getProperty("java.io.tmpdir", "/tmp");
        final String uuid = UUID.randomUUID().toString();

        /*
         * Try and create the base directory, barf is not possible
         */
        baseDir = new File(tmpdir + '/' + uuid);
        if (!baseDir.mkdirs())
            throw new CompilingException(CANNOT_CREATE_DIRECTORY);

        /*
         * Try and create the URL for the class loader, barf if not possible
         */
        final URL url;
        try {
            url = baseDir.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new CompilingException(new ProcessingMessage()
                .message(CANNOT_CREATE_URI), e);
        }

        /*
         * Create the class loader
         */
        classLoader = URLClassLoader.newInstance(new URL[] { url });
    }

    public String getDirectory()
    {
        return baseDir.toString();
    }

    public Class<?> getGeneratedClass()
        throws CompilingException
    {
        try {
            return classLoader.loadClass(fullClassName);
        } catch (ClassNotFoundException e) {
            throw new CompilingException(new ProcessingMessage()
                .message(CANNOT_FIND_CLASS).put("class", fullClassName), e);
        }
    }
}
