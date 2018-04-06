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
package org.eclipse.kapua.broker.client.hono;

import java.util.HashMap;
import java.util.Map;

public class ClientOptions {

    public enum HonoClientOptions {
        HOST,
        PORT,
        USERNAME,
        PASSWORD,
        TENANT_ID,
        NAME,
        MESSAGE_TYPE,
        TRUSTSTORE_FILE,
        MAXIMUM_RECONNECTION_ATTEMPTS,
        WAIT_BETWEEN_RECONNECT,
        CONNECT_TIMEOUT,
        IDLE_TIMEOUT,
        EXIT_CODE
    }

    private Map<String, Object> options;

    public ClientOptions() {
        options = new HashMap<>();
        //fill default values
        put(HonoClientOptions.MAXIMUM_RECONNECTION_ATTEMPTS, 10);
        put(HonoClientOptions.EXIT_CODE, -1);
    }

    public ClientOptions(String host, int port, String username, String password) {
        this();
        options = new HashMap<>();
        //fill default values
        put(HonoClientOptions.HOST, host);
        put(HonoClientOptions.PORT, port);
        put(HonoClientOptions.USERNAME, username);
        put(HonoClientOptions.PASSWORD, password);
    }

    public void put(String key, Object value) {
        options.put(key, value);
    }

    public Object get(String key) {
        return options.get(key);
    }

    public Integer getInt(HonoClientOptions key, Integer defaultValue) {
        Integer tmp = (Integer)options.get(key.name());
        return (tmp!=null ? tmp : defaultValue);
    }

    public Long getLong(HonoClientOptions key, Long defaultValue) {
        Long tmp = (Long)options.get(key.name());
        return (tmp!=null ? tmp : defaultValue);
    }

    public String getString(HonoClientOptions key) {
        return (String)options.get(key.name());
    }

    public void put(HonoClientOptions key, Object value) {
        options.put(key.name(), value);
    }

    public Object get(HonoClientOptions key) {
        return options.get(key.name());
    }

    public void clear() {
        options.clear();
    }

}
