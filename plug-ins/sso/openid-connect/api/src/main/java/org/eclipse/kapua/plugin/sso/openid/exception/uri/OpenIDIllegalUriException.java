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

import org.eclipse.kapua.KapuaErrorCode;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDErrorCodes;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDIllegalArgumentException;

/**
 * @since 1.2.0
 */
public class OpenIDIllegalUriException extends OpenIDIllegalArgumentException {

    /**
     * Constructor
     *
     * @param argName  the name of the illegal URI
     * @param argValue the value of the illegal URI
     * @since 1.2.0
     */
    public OpenIDIllegalUriException(String argName, String argValue) {
        this(OpenIDErrorCodes.ILLEGAL_URI, argName, argValue);
    }

    /**
     * Constructor
     *
     * @param code     The {@link KapuaErrorCode} associated with the {@link Exception}.
     * @param argName  the name of the illegal URI
     * @param argValue the value of the illegal URI
     * @since 1.2.0
     */
    protected OpenIDIllegalUriException(KapuaErrorCode code, String argName, String argValue) {
        super(code, argName, argValue);
    }
}
