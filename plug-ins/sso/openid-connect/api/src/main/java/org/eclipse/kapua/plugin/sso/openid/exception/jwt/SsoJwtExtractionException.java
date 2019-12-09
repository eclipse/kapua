/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.plugin.sso.openid.exception.SsoErrorCodes;

public class SsoJwtExtractionException extends SsoJwtException {

    /**
     * Constructor.
     *
     * @param cause     The original {@link Throwable}.
     * @param arguments The arguments associated with the {@link Exception}.
     */
    public SsoJwtExtractionException(Throwable cause, Object... arguments) {
        super(SsoErrorCodes.JWT_EXTRACTION_ERROR, cause, arguments);
    }
}
