package com.github.fge.jsonschema2avro;

import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.jsonpointer.JsonPointer;
import com.github.fge.jsonschema.tree.SchemaTree;
import com.github.fge.jsonschema.walk.SchemaListener;
import org.apache.avro.Schema;

public final class AvroSchemaGenerator
    implements SchemaListener<Schema>
{
    @Override
    public void onInit(final SchemaTree tree)
        throws ProcessingException
    {
    }

    @Override
    public void onNewTree(final SchemaTree oldTree, final SchemaTree newTree)
        throws ProcessingException
    {
    }

    @Override
    public void onPushd(final JsonPointer pointer)
        throws ProcessingException
    {
    }

    @Override
    public void onWalk(final SchemaTree tree)
        throws ProcessingException
    {
    }

    @Override
    public void onPopd()
        throws ProcessingException
    {
    }

    @Override
    public void onExit()
        throws ProcessingException
    {
    }

    @Override
    public Schema getValue()
    {
        return null;
    }
}
