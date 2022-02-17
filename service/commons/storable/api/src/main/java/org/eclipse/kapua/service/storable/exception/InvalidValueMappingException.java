/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.storable.exception;

/**
 * {@link MappingException} to {@code throw} when the value given to be serialized cannot be mapped to any type.
 *
 * @since 1.3.0
 */
public class InvalidValueMappingException extends MappingException {

    private final String name;
    private final Object value;
    private final Class<?> type;

    /**
     * Constructor.
     *
     * @param value The value for which mapping is not supported by the code.
     * @since 1.3.0
     */
    public InvalidValueMappingException(String name, Object value, Class<?> type) {
        this(null, name, value, type);
    }

    /**
     * Constructor.
     *
     * @param cause The root {@link Throwable} of this {@link InvalidValueMappingException}.
     * @param value The value for which mapping is not supported by the code.
     * @since 1.3.0
     */
    public InvalidValueMappingException(Throwable cause, String name, Object value, Class<?> type) {
        super(StorableErrorCodes.INVALID_VALUE, cause, value, type);

        this.name = name;
        this.value = value;
        this.type = value != null ? value.getClass() : null;
    }

    /**
     * Gets the name of the mappable value.
     *
     * @return The name of the mappable value.
     * @since 1.3.0
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the not valid value according to {@link #getType()}.
     *
     * @return The not valid value.
     * @since 1.3.0
     */
    public Object getValue() {
        return value;
    }

    /**
     * Gets the not valid {@link Class} according to {@link #getValue()}.
     *
     * @return The not valid {@link Class} according to {@link #getValue()}.
     * @since 1.3.0
     */
    public Class<?> getType() {
        return type;
    }
}
