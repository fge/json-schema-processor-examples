package com.github.fge.jsonschema2avro.predicates;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.util.NodeType;
import com.github.fge.jsonschema2avro.AvroPayload;
import com.google.common.base.Predicate;

public final class AvroPredicates
{
    private AvroPredicates()
    {
    }

    public static Predicate<AvroPayload> simpleType()
    {
        return new Predicate<AvroPayload>()
        {
            @Override
            public boolean apply(final AvroPayload input)
            {
                final JsonNode typeNode  = schemaNode(input).path("type");

                if (!typeNode.isTextual())
                    return false;

                final NodeType type = NodeType.fromName(typeNode.textValue());
                return type != NodeType.ARRAY && type != NodeType.OBJECT;
            }
        };
    }

    private static JsonNode schemaNode(final AvroPayload payload)
    {
        return payload.getTree().getNode();
    }
}
