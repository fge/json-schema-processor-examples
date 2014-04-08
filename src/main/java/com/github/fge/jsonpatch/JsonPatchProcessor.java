package com.github.fge.jsonpatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.processing.Processor;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.core.util.ValueHolder;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import java.io.IOException;

public final class JsonPatchProcessor
    implements Processor<JsonPatchInput, ValueHolder<JsonNode>>
{
    private static final JsonSchema SCHEMA;

    static {
        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        try {
            final JsonNode node = JsonLoader.fromResource("/json-patch.json");
            SCHEMA = factory.getJsonSchema(node);
        } catch (ProcessingException e) {
            throw new ExceptionInInitializerError(e);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    public ValueHolder<JsonNode> process(final ProcessingReport report,
        final JsonPatchInput input)
        throws ProcessingException
    {
        final JsonNode rawPatch = input.getRawPatch();
        report.mergeWith(SCHEMA.validate(rawPatch));
        if (!report.isSuccess())
            throw new ProcessingException(input.newMessage()
                .setMessage("illegal JSON patch"));

        try {
            final JsonPatch patch = JsonPatch.fromJson(rawPatch);
            final JsonNode ret = patch.apply(input.getNode());
            return ValueHolder.hold("result", ret);
        } catch (JsonPatchException e) {
            throw new ProcessingException(input.newMessage()
                .setMessage("failed to apply patch"), e);
        } catch (IOException e) {
            throw new ProcessingException(input.newMessage()
                .setMessage("patch considered invalid but schema validated it"),
                e);
        }
    }
}
