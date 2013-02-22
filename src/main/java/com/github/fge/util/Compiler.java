package com.github.fge.util;

import com.google.common.base.Joiner;
import com.google.common.io.Files;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Compiler
{
    private static final Joiner JOINER = Joiner.on('/');
    private static final File INPUTFILE = new File("/home/fge/tmp/foo.txt");

    private static final Pattern PACKAGE_PATTERN
        = Pattern.compile("^package\\s+(\\w+(\\.\\w+)*);", Pattern.MULTILINE);
    private static final Pattern CLASSNAME_PATTERN
        = Pattern.compile("^public\\s+(?:final\\s+)?class\\s+(\\w+)",
            Pattern.MULTILINE);

    private Compiler()
    {
    }

    public static void main(final String... args)
        throws IOException, ClassNotFoundException, InstantiationException,
        IllegalAccessException
    {
        final byte[] contents = Files.toByteArray(INPUTFILE);
        String source = new String(contents, Charset.forName("UTF-8"));
        String packageName = null, className = null;
        Matcher m;
        m = PACKAGE_PATTERN.matcher(source);
        if (m.find())
            packageName = m.group(1);
        m = CLASSNAME_PATTERN.matcher(source);
        if (m.find())
            className = m.group(1);

        if (packageName == null || className == null) {
            System.err.println("No package or class found");
            System.exit(1);
        }

        System.out.println("Package: " + packageName + ", class: " + className);

        doCompile(contents, packageName, className);
    }

    private static void doCompile(final byte[] contents,
        final String packageName, final String className)
        throws IOException, ClassNotFoundException, IllegalAccessException,
        InstantiationException
    {
        final String fullClassName = packageName + '.' + className;
        // Note: returns IllegalStateException, not ideal
        final File root = Files.createTempDir();
        final String fullFile = JOINER.join(root,
            packageName.replace('.', '/'), className + ".java");
        final File file = new File(fullFile);
        // Yeah, File sucks. Maybe I should juse use Java 7 for the demo.
        file.getParentFile().mkdirs();
        Files.write(contents, file);

        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final URLClassLoader loader
            = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });

        compiler.run(null, null, null, fullFile);
        final Class<?> c = Class.forName(fullClassName, true, loader);
        System.out.println(c.newInstance());

    }
}
