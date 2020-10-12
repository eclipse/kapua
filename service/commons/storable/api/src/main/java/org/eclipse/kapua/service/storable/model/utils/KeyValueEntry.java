/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
