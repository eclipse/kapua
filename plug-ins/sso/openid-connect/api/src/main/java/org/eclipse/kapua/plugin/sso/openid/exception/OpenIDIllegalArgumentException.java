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
package org.eclipse.kapua.plugin.sso.openid.exception;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * OpenIDIllegalArgumentException is thrown when the value of a method parameter is invalid.
 *
 * @since 1.2.0
 */
public class OpenIDIllegalArgumentException extends OpenIDException {

    private String argumentName;
    private String argumentValue;

    /**
     * Constructor
     *
     * @param argName  the name of the illegal argument
     * @param argValue the value of the illegal argument
     */
    public OpenIDIllegalArgumentException(String argName, String argValue) {
        this(OpenIDErrorCodes.ILLEGAL_ARGUMENT, argName, argValue);
    }

    /**
     * Constructor
     *
     * @param code     The {@link KapuaErrorCode} associated with the {@link Exception}.
     * @param argName  the name of the illegal argument
     * @param argValue the value of the illegal argument
     */
    protected OpenIDIllegalArgumentException(KapuaErrorCode code, String argName, String argValue) {
        super(code, argName, argValue);

        this.argumentName = argName;
        this.argumentValue = argValue;
    }

    public String getArgumentName() {
        return argumentName;
    }

    public String getArgumentValue() {
        return argumentValue;
    }
}
