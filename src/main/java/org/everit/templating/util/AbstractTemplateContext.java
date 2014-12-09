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

import org.everit.templating.TemplateContext;

public abstract class AbstractTemplateContext implements TemplateContext {

    private String mfragmentId;

    private Map<String, Object> mVars;

    public AbstractTemplateContext(final String mfragmentId, final Map<String, Object> mVars) {
        this.mfragmentId = mfragmentId;
        this.mVars = mVars;
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
        Map<String, Object> parentVars = mVars;
        String parentFragmentId = this.mfragmentId;

        mVars = new InheritantMap<String, Object>(mVars, true);
        this.mfragmentId = fragmentId;

        try {
            return renderFragmentInternal(fragmentId, mVars);
        } finally {
            mVars = parentVars;
            mfragmentId = parentFragmentId;
        }
    }

    protected abstract String renderFragmentInternal(final String fragmentId, final Map<String, Object> vars);

    public <R> R runInBlock(final Supplier<R> supplier) {
        Map<String, Object> originalVars = mVars;
        mVars = new InheritantMap<String, Object>(originalVars, true);
        try {
            return supplier.call();
        } finally {
            mVars = originalVars;
        }
    }
}