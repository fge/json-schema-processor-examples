package com.github.fge.jsonpatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.report.MessageProvider;
import com.github.fge.jsonschema.report.ProcessingMessage;

public final class JsonPatchInput
    implements MessageProvider
{
    private final JsonNode rawPatch;
    private final JsonNode node;

    public JsonPatchInput(final JsonNode rawPatch, final JsonNode node)
    {
        this.rawPatch = rawPatch;
        this.node = node;
    }

    public JsonNode getRawPatch()
    {
        return rawPatch;
    }

    public JsonNode getNode()
    {
        return node;
    }

    @Override
    public ProcessingMessage newMessage()
    {
        return new ProcessingMessage().put("patch", rawPatch).put("node", node);
    }
}
