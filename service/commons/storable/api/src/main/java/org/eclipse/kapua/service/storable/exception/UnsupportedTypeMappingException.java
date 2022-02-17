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
public class UnsupportedTypeMappingException extends MappingException {

    private final String name;
    private final Object value;
    private final Class<?> type;

    /**
     * Constructor.
     *
     * @param value The value for which mapping is not supported by the code.
     * @since 1.3.0
     */
    public UnsupportedTypeMappingException(String name, Object value) {
        super(StorableErrorCodes.UNSUPPORTED_TYPE, name, value, value != null ? value.getClass().getName() : "null");

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
     * Gets the not mappable value.
     *
     * @return The not mappable value.
     * @since 1.3.0
     */
    public Object getValue() {
        return value;
    }

    /**
     * Gets the not mappable {@link Class}.
     *
     * @return he not mappable {@link Class}.
     * @since 1.3.0
     */
    public Class<?> getType() {
        return type;
    }
}
