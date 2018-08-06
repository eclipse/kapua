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
package org.eclipse.kapua.broker.client.amqp;

import java.util.HashMap;
import java.util.Map;

public class ClientOptions {

    public enum AmqpClientOptions {
        BROKER_HOST,
        BROKER_PORT,
        USERNAME,
        PASSWORD,
        CLIENT_ID,
        DESTINATION,
        MAXIMUM_RECONNECTION_ATTEMPTS,
        WAIT_BETWEEN_RECONNECT,
        CONNECT_TIMEOUT,
        IDLE_TIMEOUT,
        AUTO_ACCEPT,
        PREFETCH_MESSAGES,
        QOS,
        EXIT_CODE
    }

    private Map<String, Object> options;

    public ClientOptions() {
        options = new HashMap<>();
        //fill default values
        put(AmqpClientOptions.MAXIMUM_RECONNECTION_ATTEMPTS, 10);
        put(AmqpClientOptions.EXIT_CODE, -1);
    }

    public ClientOptions(String host, int port, String username, String password) {
        this();
        options = new HashMap<>();
        //fill default values
        put(AmqpClientOptions.BROKER_HOST, host);
        put(AmqpClientOptions.BROKER_PORT, port);
        put(AmqpClientOptions.USERNAME, username);
        put(AmqpClientOptions.PASSWORD, password);
    }

    public void put(String key, Object value) {
        options.put(key, value);
    }

    public Object get(String key) {
        return options.get(key);
    }

    public Integer getInt(AmqpClientOptions key, Integer defaultValue) {
        Integer tmp = (Integer)options.get(key.name());
        return (tmp!=null ? tmp : defaultValue);
    }

    public Long getLong(AmqpClientOptions key, Long defaultValue) {
        Long tmp = (Long)options.get(key.name());
        return (tmp!=null ? tmp : defaultValue);
    }

    public String getString(AmqpClientOptions key) {
        return (String)options.get(key.name());
    }

    public void put(AmqpClientOptions key, Object value) {
        options.put(key.name(), value);
    }

    public Object get(AmqpClientOptions key) {
        return options.get(key.name());
    }

    public void clear() {
        options.clear();
    }

}
