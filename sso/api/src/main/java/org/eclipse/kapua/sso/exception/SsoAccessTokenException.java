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
package org.eclipse.kapua.sso.exception;

public class SsoAccessTokenException extends SsoException {

    /**
     * Constructor.
     *
     * @param cause The original {@link Throwable}.
     */
    public SsoAccessTokenException(Throwable cause) {
        super(SsoErrorCodes.ACCESS_TOKEN_ERROR, cause, (Object[]) null);
    }
}
