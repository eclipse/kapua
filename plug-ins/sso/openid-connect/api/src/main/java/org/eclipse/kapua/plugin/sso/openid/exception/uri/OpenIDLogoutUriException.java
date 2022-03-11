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
package org.eclipse.kapua.plugin.sso.openid.exception.uri;

import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDErrorCodes;

/**
 * @since 1.2.0
 */
public class OpenIDLogoutUriException extends OpenIDUriException {

    /**
     * Constructor.
     *
     * @param cause The original {@link Throwable}.
     * @since 1.2.0
     */
    public OpenIDLogoutUriException(Throwable cause) {
        super(OpenIDErrorCodes.LOGOUT_URI_ERROR, cause);
    }
}
