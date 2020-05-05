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
