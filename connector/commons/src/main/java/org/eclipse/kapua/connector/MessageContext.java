/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.connector;

import java.util.HashMap;
import java.util.Map;

/**
 * Message context
 *
 * @param <M> message type
 */
public class MessageContext<M> {

    private M message;
    private Map<String, Object> properties;

    public MessageContext(M message) {
        this.message = message;
        properties = new HashMap<>();
    }

    public MessageContext(M message, Map<String, Object> properties) {
        this.message = message;
        this.properties = properties;
    }

    public M getMessage() {
        return message;
    }

    public void setMessage(M message) {
        this.message = message;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public Object putProperty(String key, Object value) {
        return properties.put(key, value);
    }

}
