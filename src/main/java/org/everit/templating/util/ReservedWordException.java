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

/**
 * Exception that is thrown when someone wants to override a reserved variable.
 */
public class ReservedWordException extends RuntimeException {

    /**
     * .
     */
    private static final long serialVersionUID = -7338903497703336906L;

    public ReservedWordException(final String message) {
        super(message);
    }

}
