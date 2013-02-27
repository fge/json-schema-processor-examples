package com.github.fge.avro;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.jsonpointer.JsonPointer;
import com.github.fge.jsonschema.util.JacksonUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
final class MutableTree
{
    private static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();

    private final ObjectNode baseNode = FACTORY.objectNode();

    private JsonPointer pointer = JsonPointer.empty();

    private ObjectNode currentNode = baseNode;

    private final Multimap<JsonPointer, String> names
        = ArrayListMultimap.create();

    void put(final String fieldName, final String value)
    {
        baseNode.put(fieldName, value);
    }

    ObjectNode getBaseNode()
    {
        return baseNode;
    }

    public ObjectNode getCurrentNode()
    {
        return currentNode;
    }
}

