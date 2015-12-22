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

/**
 * Standard exception thrown for all general compileShared and some runtime failures.
 */
public class CompileException extends RuntimeException {

  /**
   * .
   */
  private static final long serialVersionUID = 1248035313147803625L;

  private static final int SHOWN_TEXT_AFTER_EXCEPTION_POSITION = 30;

  private static final int SHOWN_TEXT_BEFORE_EXCEPTION_POSITION = 20;

  private static boolean isWhitespace(final char c) {
    return c <= '\u0020';
  }

  private static String repeatChar(final char c, final int times) {
    char[] n = new char[times];
    for (int i = 0; i < times; i++) {
      n[i] = c;
    }
    return new String(n);
  }

  private int column = 0;

  private int cursor = 0;

  private char[] expr;

  private int lineNumber = 1;

  private int msgOffset = 0;

  /**
   * Constructor.
   *
   * @param message
   *          The message of the exception.
   * @param expr
   *          The expression.
   * @param cursor
   *          The place of the exception within the expression.
   */
  public CompileException(final String message, final char[] expr, final int cursor) {
    super(message);
    this.expr = expr;
    this.cursor = cursor;
  }

  /**
   * Constructor.
   *
   * @param message
   *          The message of the exception.
   * @param expr
   *          The expression.
   * @param cursor
   *          The place of the exception within the expression.
   * @param e
   *          The original cause of the exception.
   */
  public CompileException(final String message, final char[] expr, final int cursor,
      final Throwable e) {
    super(message, e);
    this.expr = expr;
    this.cursor = cursor;
  }

  private void calcRowAndColumn() {
    if (lineNumber > 1 || column > 1) {
      return;
    }

    int row = 1;
    int col = 1;

    if ((lineNumber != 0 && column != 0) || expr == null || expr.length == 0) {
      return;
    }

    for (int i = 0; i < cursor && i < expr.length; i++) {
      switch (expr[i]) {
        case '\r':
          continue;
        case '\n':
          row++;
          col = 1;
          break;

        default:
          col++;
      }
    }

    this.lineNumber = row;
    this.column = col;
  }

  private String calculateCs(final String pCs, final String match) {
    int firstCr;
    int lastCr;
    String cs = pCs;
    do {
      firstCr = cs.indexOf('\n');
      lastCr = cs.lastIndexOf('\n');

      if (firstCr == -1) {
        break;
      }

      int matchIndex = match == null ? 0 : cs.indexOf(match);

      if (firstCr == lastCr) {
        if (firstCr > matchIndex) {
          cs = cs.substring(0, firstCr);
        } else if (firstCr < matchIndex) {
          cs = cs.substring(firstCr + 1, cs.length());
        }
      } else if (firstCr < matchIndex) {
        cs = cs.substring(firstCr + 1, lastCr);
      } else {
        cs = cs.substring(0, firstCr);
      }
    } while (true);
    return cs;
  }

  private String calculateMatch(final char[] expr, final int matchStart) {
    String match;
    match = new String(expr, matchStart, expr.length - matchStart);
    Makematch: for (int i = 0; i < match.length(); i++) {
      char charAt = match.charAt(i);
      if (charAt == '\n' || charAt == ')') {
        match = match.substring(0, i);
        break Makematch;
      }
    }

    if (match.length() >= SHOWN_TEXT_AFTER_EXCEPTION_POSITION) {
      match = match.substring(0, SHOWN_TEXT_AFTER_EXCEPTION_POSITION);
    }
    return match;
  }

  private String generateErrorMessage() {
    StringBuilder appender = new StringBuilder().append("[Error: " + super.getMessage() + "]\n");

    int offset = appender.length();

    appender.append("[Near : {... ");

    offset = appender.length() - offset;

    appender.append(showCodeNearError(expr, cursor))
        .append(" ....}]\n")
        .append(repeatChar(' ', offset));

    if (msgOffset < 0) {
      msgOffset = 0;
    }

    appender.append(repeatChar(' ', msgOffset)).append('^');

    calcRowAndColumn();

    if (lineNumber != -1) {
      appender.append('\n')
          .append("[Line: " + lineNumber + ", Column: " + (column) + "]");
    }
    return appender.toString();
  }

  public CharSequence getCodeNearError() {
    return showCodeNearError(expr, cursor);
  }

  public int getColumn() {
    return column;
  }

  public int getCursor() {
    return cursor;
  }

  public int getCursorOffet() {
    return this.msgOffset;
  }

  public char[] getExpr() {
    return expr.clone();
  }

  public int getLineNumber() {
    return lineNumber;
  }

  @Override
  public String getMessage() {
    return generateErrorMessage();
  }

  private void resolveMsgOffset(final int cursor, final String cs, final int matchOffset,
      final String match,
      final String trimmed) {
    if (match != null) {
      msgOffset = trimmed.indexOf(match) + matchOffset;
    } else {
      msgOffset = cs.length() - (cs.length() - trimmed.length());
    }

    if (msgOffset == 0 && matchOffset == 0) {
      msgOffset = cursor;
    }
  }

  public void setColumn(final int column) {
    this.column = column;
  }

  public void setCursor(final int cursor) {
    this.cursor = cursor;
  }

  public void setExpr(final char[] expr) {
    this.expr = expr;
  }

  public void setLineNumber(final int lineNumber) {
    this.lineNumber = lineNumber;
  }

  private CharSequence showCodeNearError(final char[] expr, final int cursor) {
    if (expr == null) {
      return "Unknown";
    }

    int start = cursor - SHOWN_TEXT_BEFORE_EXCEPTION_POSITION;
    int end = (cursor + SHOWN_TEXT_AFTER_EXCEPTION_POSITION);

    if (end > expr.length) {
      end = expr.length;
      start -= SHOWN_TEXT_AFTER_EXCEPTION_POSITION;
    }

    if (start < 0) {
      start = 0;
    }

    String cs;

    cs = String.copyValueOf(expr, start, end - start);

    int matchStart = -1;
    int matchOffset = 0;
    String match = null;

    if (cursor < end) {
      matchStart = cursor;
      while (matchStart > 0 && !isWhitespace(expr[matchStart - 1])) {
        matchStart--;
      }

      matchOffset = cursor - matchStart;

      match = calculateMatch(expr, matchStart);
    }

    cs = calculateCs(cs, match);

    String trimmed = cs.trim();

    resolveMsgOffset(cursor, cs, matchOffset, match, trimmed);

    return trimmed;
  }

  @Override
  public String toString() {
    return generateErrorMessage();
  }
}
