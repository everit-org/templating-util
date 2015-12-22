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

import java.util.Map;

import org.everit.templating.TemplateContext;

/**
 * Helper class to be able to create template context implementations.
 */
public abstract class AbstractTemplateContext implements TemplateContext {

  /**
   * See {@link #getFragmentId()}.
   */
  private String mfragmentId;

  /**
   * See {@link #getVars()}.
   */
  private Map<String, Object> mVars;

  /**
   * Constructor.
   *
   * @param fragmentId
   *          The id of the fragment that was passed when
   *          {@link org.everit.templating.CompiledTemplate#render(java.io.Writer, Map, String)} was
   *          called.
   * @param vars
   *          The variables that can be used to evaluate expressions.
   */
  public AbstractTemplateContext(final String fragmentId, final Map<String, Object> vars) {
    this.mfragmentId = fragmentId;
    this.mVars = vars;
  }

  @Override
  public String getFragmentId() {
    return mfragmentId;
  }

  public Map<String, Object> getVars() {
    return mVars;
  }

  @Override
  public String renderFragment(final String fragmentId) {
    return renderFragment(fragmentId, null);
  }

  @Override
  public String renderFragment(final String fragmentId, final Map<String, Object> parameters) {
    Map<String, Object> parentVars = mVars;
    String parentFragmentId = this.mfragmentId;

    mVars = new InheritantMap<String, Object>(mVars, false);
    if (parameters != null) {
      mVars.putAll(parameters);
    }
    this.mfragmentId = fragmentId;

    try {
      return renderFragmentInternal(fragmentId, mVars);
    } finally {
      mVars = parentVars;
      mfragmentId = parentFragmentId;
    }
  }

  /**
   * Classes that inherit from this class should implement this function to be able to render
   * fragments via the templateContext.
   *
   * @param fragmentId
   *          The id of the fragment that should be rendered.
   * @param vars
   *          Variables that should be used during rendering the fragment. Change of this map does
   *          not affect the available variables outside the fragment.
   * @return The render output.
   */
  protected abstract String renderFragmentInternal(final String fragmentId,
      final Map<String, Object> vars);

}
