package com.github.fge.jsonschema2avro.predicates;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.processors.validation.ArraySchemaDigester;
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

    public static Predicate<AvroPayload> array()
    {
        return new Predicate<AvroPayload>()
        {
            @Override
            public boolean apply(final AvroPayload input)
            {
                final JsonNode node = schemaNode(input);
                final JsonNode typeNode = node.path("type");

                if (!typeNode.isTextual())
                    return false;

                final NodeType type = NodeType.fromName(typeNode.textValue());

                if (type != NodeType.ARRAY)
                    return false;

                final JsonNode digest
                    = ArraySchemaDigester.getInstance().digest(node);

                // FIXME: I should probably make digests POJOs here
                final boolean hasItems = digest.get("hasItems").booleanValue();
                final boolean itemsIsArray = digest.get("itemsIsArray")
                    .booleanValue();
                final boolean hasAdditional = digest.get("hasAdditional")
                    .booleanValue();

                if (!hasItems)
                    return hasAdditional;
                return !(itemsIsArray || hasAdditional);
            }
        };
    }

    private static JsonNode schemaNode(final AvroPayload payload)
    {
        return payload.getTree().getNode();
    }
}
