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
package org.eclipse.kapua.consumer.activemq.error.settings;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * ActiveMQErrorSettingsKey keys.
 * 
 * @since 1.0
 */
public enum ActiveMQErrorSettingsKey implements SettingKey {

    /**
     * Connection name (or ip)
     */
    CONNECTION_HOST("activemq_error.connection.host"),
    /**
     * Connection url
     */
    CONNECTION_PORT("activemq_error.connection.port"),
    /**
     * Connection username
     */
    CONNECTION_USERNAME("activemq_error.connection.username"),
    /**
     * Connection password
     */
    CONNECTION_PASSWORD("activemq_error.connection.password"),
    /**
     * Message client id
     */
    CLIENT_ID("activemq_error.connection.client_id"),
    /**
     * Error message destination to subscribe
     */
    DESTINATION("activemq_error.connection.destination"),
    /**
     * Wait between reconnection attempts (milliseconds)
     */
    WAIT_BETWEEN_RECONNECT("activemq_error.connection.wait_between_reconnect"),
    /**
     * Connection timeout (milliseconds)
     */
    CONNECT_TIMEOUT("activemq_error.connection.connect_timeout"),
    /**
     * Idle timeout (seconds)
     */
    IDLE_TIMEOUT("activemq_error.connection.idle_timeout"),
    /**
     * Maximum reconnection attempt (without any success between them) before exiting JVM (negative numbers means no exit)
     */
    MAX_RECONNECTION_ATTEMPTS("activemq_error.maximum_reconnection_attempt"),
    /**
     * Exiting code when maximum reconnection attempt is reached
     */
    EXIT_CODE("activemq_error.exit_code");

    private String key;

    private ActiveMQErrorSettingsKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
