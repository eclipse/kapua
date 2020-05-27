/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua;

/**
 * KapuaIllegalArgumentException is thrown when the value of a method parameter is invalid.
 *
 * @since 1.0
 *
 */
public class KapuaIllegalArgumentException extends KapuaException {

    private static final long serialVersionUID = -7067191169730223113L;

    private String argumentName;
    private String argumentValue;

    /**
     * Constructor
     *
     * @param argName
     * @param argValue
     */
    public KapuaIllegalArgumentException(String argName, String argValue) {
        this(KapuaErrorCodes.ILLEGAL_ARGUMENT, argName, argValue);
    }

    /**
     * Constructor
     *
     * @param code
     * @param argName
     * @param argValue
     */
    protected KapuaIllegalArgumentException(KapuaErrorCodes code, String argName, String argValue) {
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
