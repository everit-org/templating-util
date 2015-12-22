/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.biz)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.templating.util;

import java.io.IOException;
import java.io.Writer;

/**
 * Wrapper around a {@link Writer} that throws unchecked exception instead of {@link IOException}.
 */
public class TemplateWriter {

  /**
   * The wrapped writer.
   */
  private final Writer writer;

  public TemplateWriter(final Writer writer) {
    this.writer = writer;
  }

  /**
   * Appending some text to the writer. See {@link Writer#append(CharSequence)}.
   *
   * @param text
   *          The text to append.
   * @return The instance of this {@link TemplateWriter}.
   */
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
