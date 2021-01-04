/*******************************************************************************
 * Copyright (c) 2019, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.plugin.sso.openid.exception;

public class OpenIDAccessTokenException extends OpenIDException {

    /**
     * Constructor.
     *
     * @param cause The original {@link Throwable}.
     */
    public OpenIDAccessTokenException(Throwable cause) {
        super(OpenIDErrorCodes.ACCESS_TOKEN_ERROR, cause, (Object[]) null);
    }
}
