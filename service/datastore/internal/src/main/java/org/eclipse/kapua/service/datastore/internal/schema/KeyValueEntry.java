/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.schema;

/**
 * Field entry
 * 
 * @since 1.0
 */
public class KeyValueEntry {

    private String key;
    private Object value;

    /**
     * Construct a key/value entry with the provided key and value
     * 
     * @param key
     * @param value
     */
    public KeyValueEntry(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Get the key
     * 
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     * Get the value
     * 
     * @return
     */
    public Object getValue() {
        return value;
    }

}
