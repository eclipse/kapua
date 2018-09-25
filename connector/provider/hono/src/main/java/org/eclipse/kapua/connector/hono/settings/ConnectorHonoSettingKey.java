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
package org.eclipse.kapua.connector.hono.settings;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * ConnectorHono settings
 */
public enum ConnectorHonoSettingKey implements SettingKey {

    /**
     * Hono connection configuration - username
     */
    HONO_USERNAME("connector.hono.username"),
    /**
     * Hono connection configuration - password
     */
    HONO_PASSWORD("connector.hono.password"),
    /**
     * Hono connection configuration - tenant id (comma separated tenant id list)
     */
    HONO_TENANT_ID("connector.hono.tenant_id"),
    /**
     * Hono connection configuration - host
     */
    HONO_HOST("connector.hono.host"),
    /**
     * Hono connection configuration - port
     */
    HONO_PORT("connector.hono.port"),
    /**
     * Maximum reconnection attempts (-1 means no limit)
     */
    HONO_PROTON_MAX_RECONNECT_ATTEMPTS("connector.hono.proton.max_reconnect_attempts"),
    /**
     * Wait between reconnection attempts
     */
    HONO_PROTON_WAIT_BETWEEN_RECONNECT("connector.hono.proton.wait_between_reconnection"),
    /**
     * Connection timeout (in seconds)
     */
    HONO_PROTON_CONNECT_TIMEOUT("connector.hono.proton.connect_timeout"),
    /**
     * idle timeout (in milliseconds)
     */
    HONO_PROTON_IDLE_TIMEOUT("connector.hono.proton.idle_timeout"),
    /**
     * Maximum reconnection attempt (without any success between them) before exiting JVM (negative numbers means no exit)
     */
    MAX_RECONNECTION_ATTEMPTS("connector.hono.maximum_reconnection_attempt"),
    /**
     * Exiting code when maximum reconnection attempt is reached
     */
    EXIT_CODE("connector.hono.exit_code"),
    /**
     * Hono connection configuration - trutstore file
     */
    HONO_TRUSTSTORE_FILE("connector.hono.truststore_file");

    private String key;

    private ConnectorHonoSettingKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
