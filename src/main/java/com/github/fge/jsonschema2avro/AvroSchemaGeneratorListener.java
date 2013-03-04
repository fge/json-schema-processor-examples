package com.github.fge.jsonschema2avro;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.jsonpointer.JsonPointer;
import com.github.fge.jsonschema.tree.SchemaTree;
import com.github.fge.jsonschema.util.NodeType;
import com.github.fge.jsonschema.walk.SchemaListener;
import com.github.fge.jsonschema2avro.generators.AvroGenerator;
import com.github.fge.jsonschema2avro.generators.AvroGenerators;
import com.google.common.collect.ImmutableMap;
import org.apache.avro.Schema;

import java.util.Map;

public final class AvroSchemaGeneratorListener
    implements SchemaListener<Schema>
{
    private static final Map<NodeType, Schema.Type> PRIMITIVE_MAP;

    static {
        PRIMITIVE_MAP = ImmutableMap.<NodeType, Schema.Type>builder()
            .put(NodeType.BOOLEAN, Schema.Type.BOOLEAN)
            .put(NodeType.INTEGER, Schema.Type.LONG)
            .put(NodeType.NULL, Schema.Type.NULL)
            .put(NodeType.NUMBER, Schema.Type.DOUBLE)
            .put(NodeType.STRING, Schema.Type.STRING)
            .build();
    }

    private Schema schema;

    @Override
    public void onInit(final SchemaTree tree)
        throws ProcessingException
    {
        final JsonNode node = tree.getNode();
        final JsonNode typeNode = node.path("type");
        if (!typeNode.isTextual())
            throw new UnsupportedJsonSchemaException();
        final NodeType type = NodeType.fromName(typeNode.textValue());
        final Schema.Type avroType = PRIMITIVE_MAP.get(type);
        if (avroType == null)
            throw new UnsupportedJsonSchemaException();
        final AvroGenerator generator = AvroGenerators.get(avroType);
        if (generator == null)
            throw new UnsupportedJsonSchemaException();
        schema = generator.generate();
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
        return schema;
    }
}
