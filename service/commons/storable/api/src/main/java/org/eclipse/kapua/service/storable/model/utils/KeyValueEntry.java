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
package org.eclipse.kapua.service.storable.model.utils;

/**
 * Field entry
 *
 * @since 1.0.0
 */
public class KeyValueEntry {

    private final String key;
    private final Object value;

    /**
     * Construct a key/value entry with the provided key and value
     *
     * @param key   The key of the field.
     * @param value The value of the field.
     * @since 1.0.0
     */
    public KeyValueEntry(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Gets the key of the field.
     *
     * @return The key of the field.
     * @since 1.0.0
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the value of the field.
     *
     * @return The value of the field.
     * @since 1.0.0
     */
    public Object getValue() {
        return value;
    }

}
