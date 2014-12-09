package org.everit.templating.util;

import java.io.IOException;
import java.io.Writer;

public class TemplateWriter {

    private final Writer writer;

    public TemplateWriter(final Writer writer) {
        this.writer = writer;
    }

    public TemplateWriter append(final String text) {
        try {
            this.writer.write(text);
        } catch (IOException e) {
            // If it was java 8, we would throw an UncheckedIOException.
            throw new RuntimeException(e);
        }
        return this;
    }

    public Writer getWrapped() {
        return writer;
    }

}
