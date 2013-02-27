package com.github.fge.avro;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.jsonpointer.JsonPointer;
import com.github.fge.jsonschema.util.AsJson;
import com.github.fge.jsonschema.util.JacksonUtils;
import com.github.fge.jsonschema.util.NodeType;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public final class MutableTree
    implements AsJson
{
    private static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();

    private final ObjectNode baseNode = FACTORY.objectNode();

    private JsonPointer pointer = JsonPointer.empty();

    private ObjectNode currentNode = baseNode;

    private final Multimap<JsonPointer, String> names
        = ArrayListMultimap.create();

    public void setType(final NodeType type)
    {
        currentNode.put("type", type.toString());
    }

    public ObjectNode getBaseNode()
    {
        return baseNode;
    }

    public ObjectNode getCurrentNode()
    {
        return currentNode;
    }

    public JsonPointer getPointer()
    {
        return pointer;
    }

    public void setPointer(final JsonPointer pointer)
    {
        this.pointer = pointer;
        currentNode = (ObjectNode) pointer.get(baseNode);
    }

    @Override
    public JsonNode asJson()
    {
        return FACTORY.objectNode().put("pointer", pointer.toString());
    }
}

