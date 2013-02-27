package com.github.fge.avro;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ObjectNodeWriter
{
    void writeTo(final ObjectNode node);
}
