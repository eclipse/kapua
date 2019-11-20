/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.sso.exception.uri;

import org.eclipse.kapua.KapuaErrorCode;
import org.eclipse.kapua.sso.exception.SsoException;

public abstract class SsoUriException extends SsoException {

    /**
     * Builds a new KapuaException instance based on the supplied KapuaErrorCode,
     * an Throwable cause, and optional arguments for the associated exception message.
     *
     * @param code      The {@link KapuaErrorCode} associated with the {@link Exception}.
     * @param cause     The original {@link Throwable}.
     * @param arguments The arguments associated with the {@link Exception}.
     * @since 1.2.0
     */
    public SsoUriException(KapuaErrorCode code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }
}
