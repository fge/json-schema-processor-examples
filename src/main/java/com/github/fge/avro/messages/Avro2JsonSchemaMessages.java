package com.github.fge.avro.messages;

public enum Avro2JsonSchemaMessages
{
    UNSUPPORTED_TYPE("type is not supported (yet)"),
    BRAINDEAD_LANGUAGES("some languages have no support for long"),
    ;

    private final String message;

    Avro2JsonSchemaMessages(final String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return message;
    }
}
