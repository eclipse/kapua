/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua;

/**
 * KapuaIllegalArgumentException is thrown when the value of a method parameter is invalid.
 *
 * @since 1.0.0
 */
public class KapuaIllegalArgumentException extends KapuaException {

    private static final long serialVersionUID = -7067191169730223113L;

    private final String argumentName;
    private final String argumentValue;

    /**
     * Constructor.
     * <p>
     * This is {@code protected} to handle {@link KapuaIllegalArgumentException} and {@link KapuaIllegalNullArgumentException} nicely.
     *
     * @param code     The {@link KapuaErrorCode}.
     * @param argumentName  The illegal argument name.
     * @param argumentValue The illegal argument value.
     * @since 1.0.0
     */
    protected KapuaIllegalArgumentException(KapuaErrorCodes code, String argumentName, String argumentValue) {
        super(code, argumentName, argumentValue);

        this.argumentName = argumentName;
        this.argumentValue = argumentValue;
    }

    /**
     * Constructor.
     *
     * @param argumentName  The illegal argument name.
     * @param argumentValue The illegal argument value.
     * @since 1.0.0
     */
    public KapuaIllegalArgumentException(String argumentName, String argumentValue) {
        this(KapuaErrorCodes.ILLEGAL_ARGUMENT, argumentName, argumentValue);
    }

    /**
     * Gest the illegal argument name.
     *
     * @return The illegal argument name.
     * @since 1.0.0
     */
    public String getArgumentName() {
        return argumentName;
    }

    /**
     * Gets the illegal argument value.
     *
     * @return The illegal argument value.
     * @since 1.0.0
     */
    public String getArgumentValue() {
        return argumentValue;
    }
}
