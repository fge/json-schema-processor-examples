package com.github.fge.jsonschema2avro.predicates;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.processors.validation.ArraySchemaDigester;
import com.github.fge.jsonschema.processors.validation.ObjectSchemaDigester;
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
                final JsonNode node = schemaNode(input);
                final NodeType type = getType(node);
                if (type == null)
                    return false;
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
                final NodeType type = getType(node);
                if (NodeType.ARRAY != type)
                    return false;

                final JsonNode digest
                    = ArraySchemaDigester.getInstance().digest(node);

                // FIXME: I should probably make digests POJOs here
                return digest.get("hasItems").booleanValue()
                    ? !digest.get("itemsIsArray").booleanValue()
                    : digest.get("hasAdditional").booleanValue();
            }
        };
    }

    public static Predicate<AvroPayload> map()
    {
        return new Predicate<AvroPayload>()
        {
            @Override
            public boolean apply(final AvroPayload input)
            {
                final JsonNode node = schemaNode(input);
                final NodeType type = getType(node);
                if (NodeType.OBJECT != type)
                    return false;

                final JsonNode digest = ObjectSchemaDigester.getInstance()
                    .digest(node);

                // FIXME: as for array digester, the result should really be
                // a POJO
                if (!digest.get("hasAdditional").booleanValue())
                    return false;

                return digest.get("properties").size() == 0
                    && digest.get("patternProperties").size() == 0;
            }
        };
    }

    private static JsonNode schemaNode(final AvroPayload payload)
    {
        return payload.getTree().getNode();
    }

    private static NodeType getType(final JsonNode node)
    {
        final JsonNode typeNode = node.path("type");
        return typeNode.isTextual() ? NodeType.fromName(typeNode.textValue())
            : null;
    }
}
