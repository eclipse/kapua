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

    enum HonoClientOptions {
        HOST,
        PORT,
        USERNAME,
        PASSWORD,
        TENANT_ID,
        DESTINATION,
        TRUSTSTORE_FILE,
        MAXIMUM_RECONNECTION_ATTEMPTS,
        WAIT_BETWEEN_RECONNECT,
        CONNECT_TIMEOUT,
        IDLE_TIMEOUT,
        EXIT_CODE
    }

    //example values
    /*
hono_lifecycle.connection.host=127.0.0.1
hono_lifecycle.connection.port=5672
hono_lifecycle.connection.username=username
hono_lifecycle.connection.password=password
hono_lifecycle.connection.telemetry_client_id=hono-lifecycle
hono_lifecycle.connection.telemetry_destination=
hono_lifecycle.connection.error_client_id=hono-lifecycle-error
hono_lifecycle.connection.error_destination=
#milliseconds
hono_lifecycle.connection.wait_between_reconnect=1000
#milliseconds
hono_lifecycle.connection.connect_timeout=5000
#seconds
hono_lifecycle.connection.idle_timeout=300
hono_lifecycle.maximum_reconnection_attempt=10
hono_lifecycle.exit_code=-1
     */

    private Map<String, Object> options;

    public ClientOptions() {
        options = new HashMap<>();
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
