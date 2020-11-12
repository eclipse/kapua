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
package org.eclipse.kapua.sso.exception.uri;

import org.eclipse.kapua.sso.exception.SsoErrorCodes;

public class SsoJwtUriException extends SsoUriException {

    /**
     * Constructor.
     *
     * @param cause The original {@link Throwable}.
     */
    public SsoJwtUriException(Throwable cause) {
        super(SsoErrorCodes.JWT_URI_ERROR, cause);
    }
}
