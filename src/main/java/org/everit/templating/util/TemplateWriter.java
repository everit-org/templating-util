/**
 * This file is part of Everit - Templating Util.
 *
 * Everit - Templating Util is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Everit - Templating Util is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Everit - Templating Util.  If not, see <http://www.gnu.org/licenses/>.
 */
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
