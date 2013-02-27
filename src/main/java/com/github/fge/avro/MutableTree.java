package com.github.fge.avro;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.jsonpointer.JsonPointer;
import com.github.fge.jsonschema.util.AsJson;
import com.github.fge.jsonschema.util.JacksonUtils;
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

    public void put(final String fieldName, final String value)
    {
        baseNode.put(fieldName, value);
    }

    public ObjectNode getBaseNode()
    {
        return baseNode;
    }

    public ObjectNode getCurrentNode()
    {
        return currentNode;
    }

    @Override
    public JsonNode asJson()
    {
        return FACTORY.objectNode().put("pointer", pointer.toString());
    }
}

