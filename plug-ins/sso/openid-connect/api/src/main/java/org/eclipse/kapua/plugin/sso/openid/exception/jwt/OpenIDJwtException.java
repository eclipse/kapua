/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.plugin.sso.openid.exception.jwt;

import org.eclipse.kapua.KapuaErrorCode;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDException;

/**
 * @since 1.2.0
 */
public abstract class OpenIDJwtException extends OpenIDException {

    /**
     * Constructor.
     *
     * @param code The {@link KapuaErrorCode} associated with the {@link Exception}
     * @since 1.2.0
     */
    public OpenIDJwtException(KapuaErrorCode code) {
        super(code);
    }

    /**
     * Builds a new KapuaException instance based on the supplied KapuaErrorCode,
     * an Throwable cause, and optional arguments for the associated exception message.
     *
     * @param code      The {@link KapuaErrorCode} associated with the {@link Exception}.
     * @param cause     The original {@link Throwable}.
     * @param arguments The arguments associated with the {@link Exception}.
     * @since 1.2.0
     */
    public OpenIDJwtException(KapuaErrorCode code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }
}
