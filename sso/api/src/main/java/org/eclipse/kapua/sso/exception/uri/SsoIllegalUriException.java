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

import org.eclipse.kapua.KapuaErrorCode;
import org.eclipse.kapua.sso.exception.SsoErrorCodes;
import org.eclipse.kapua.sso.exception.SsoIllegalArgumentException;

public class SsoIllegalUriException extends SsoIllegalArgumentException {

    /**
     * Constructor
     *
     * @param argName  the name of the illegal URI
     * @param argValue the value of the illegal URI
     */
    public SsoIllegalUriException(String argName, String argValue) {
        this(SsoErrorCodes.ILLEGAL_URI, argName, argValue);
    }

    /**
     * Constructor
     *
     * @param code     The {@link KapuaErrorCode} associated with the {@link Exception}.
     * @param argName  the name of the illegal URI
     * @param argValue the value of the illegal URI
     */
    protected SsoIllegalUriException(KapuaErrorCode code, String argName, String argValue) {
        super(code, argName, argValue);
    }
}
