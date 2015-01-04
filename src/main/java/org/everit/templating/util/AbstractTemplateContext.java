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

import java.util.Map;

import org.everit.templating.CompiledTemplate;
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
     *            The id of the fragment that was passed when
     *            {@link CompiledTemplate#render(java.io.Writer, Map, String)} was called.
     * @param vars
     *            The variables that can be used to evaluate expressions.
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
     * Classes that inherit from this class should implement this function to be able to render fragments via the
     * templateContext.
     *
     * @param fragmentId
     *            The id of the fragment that should be rendered.
     * @param vars
     *            Variables that should be used during rendering the fragment. Change of this map does not affect the
     *            available variables outside the fragment.
     * @return The render output.
     */
    protected abstract String renderFragmentInternal(final String fragmentId, final Map<String, Object> vars);

}
