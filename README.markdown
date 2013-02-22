<h2>Read me first</h2>

<p>The license of this project is LGPLv3 or later. See file src/main/resources/LICENSE for the full
text.</p>

<h2>What this is</h2>

<p>This package contains sample demonstration processors based on <a
href="https://github.com/fge/json-schema-core">json-schema-core</a>.</p>

<h2>List of processors</h2>

<h3>Java class to JSON Schema</h3>

<a
href="https://github.com/fge/json-schema-processor-examples/blob/master/src/main/java/com/github/fge/jjschema/JJSchemaProcessor.java">link
to source code</a>

<p>This processor highlights a very simple use of <a
href="https://github.com/reinert/JJSchema">JJSchema</a> to generate a JSON Schema (as a `JsonNode`)
from a class.</p>

<h3>Validating using a Java class</h3>

<a
href="https://github.com/fge/json-schema-processor-examples/blob/master/src/main/java/com/github/fge/jjschema/JJSchemaValidator.java">link
to source code</a>


<p>This processor still uses <a href="https://github.com/reinert/JJSchema">JJSchema</a> and the
above processor to generate a schema out of a class, but then plugs it into the main validation
processor of <a href="https://github.com/fge/json-schema-validator">json-schema-validator</a> to
chain this with validation (and error reporting) of actual JSON instances.</p>

<h2>Maven artifact</h2>

<p>Replace <tt>your-version-here</tt> with the appropriate version:</p>

```xml
<dependency>
    <groupId>com.github.fge</groupId>
    <artifactId>json-schema-processor-examples</artifactId>
    <version>your-version-here</version>
</dependency>
```

