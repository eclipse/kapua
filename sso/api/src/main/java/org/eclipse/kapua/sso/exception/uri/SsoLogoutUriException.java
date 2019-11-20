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

import org.eclipse.kapua.sso.exception.SsoErrorCodes;

public class SsoLogoutUriException extends SsoUriException {

    /**
     * Constructor.
     *
     * @param cause The original {@link Throwable}.
     */
    public SsoLogoutUriException(Throwable cause) {
        super(SsoErrorCodes.LOGOUT_URI_ERROR, cause);
    }
}
