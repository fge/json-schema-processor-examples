package com.github.fge.refexpand;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.jsonpointer.JsonPointer;
import com.github.fge.jsonschema.tree.SchemaTree;
import com.github.fge.jsonschema.walk.SchemaListener;

public final class SchemaExpander
    implements SchemaListener<JsonNode>
{
    private final MutableTree mutableTree = new MutableTree();

    @Override
    public void onInit(final SchemaTree tree)
        throws ProcessingException
    {
        mutableTree.setCurrentNode(tree.getNode().deepCopy());
    }

    @Override
    public void onNewTree(final SchemaTree oldTree, final SchemaTree newTree)
        throws ProcessingException
    {
        mutableTree.setCurrentNode(newTree.getNode().deepCopy());
    }

    @Override
    public void onPushd(final JsonPointer pointer)
        throws ProcessingException
    {
        mutableTree.pushd(pointer);
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
        mutableTree.popd();
    }

    @Override
    public void onExit()
        throws ProcessingException
    {
    }

    @Override
    public JsonNode getValue()
    {
        return mutableTree.getBaseNode();
    }
}
