package com.github.fge.jsonschema2avro;

public enum AvroMessages
{
    UNSUPPORTED_KEYWORD("keyword not supported"),
    ;
    private final String message;

    AvroMessages(final String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return message;
    }
}
