/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua;

/**
 * KapuaIllegalNullArgumentException is thrown when <tt>null</tt> is passed to a method for an argument
 * or as a value for field in an object where <tt>null</tt> is not allowed.<br>
 * This should always be used instead of <tt>NullPointerException</tt> as the latter is too easily confused with programming bugs.
 *
 * @since 1.0
 *
 */
public class KapuaIllegalNullArgumentException extends KapuaIllegalArgumentException {

    private static final long serialVersionUID = -8762712571192128282L;

    /**
     * Constructor
     *
     * @param argumentName
     */
    public KapuaIllegalNullArgumentException(String argumentName) {
        super(KapuaErrorCodes.ILLEGAL_NULL_ARGUMENT, argumentName, null);
    }
}
