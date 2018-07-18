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
package org.eclipse.kapua.consumer.activemq.lifecycle.settings;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * ActiveMQDatastoreSettingsKey keys.
 * 
 * @since 1.0
 */
public enum ActiveMQLifecycleSettingsKey implements SettingKey {

    /**
     * Connection name (or ip)
     */
    CONNECTION_HOST("activemq_lifecycle.connection.host"),
    /**
     * Connection url
     */
    CONNECTION_PORT("activemq_lifecycle.connection.port"),
    /**
     * Connection username
     */
    CONNECTION_USERNAME("activemq_lifecycle.connection.username"),
    /**
     * Connection password
     */
    CONNECTION_PASSWORD("activemq_lifecycle.connection.password"),
    /**
     * Telemetry message client id
     */
    TELEMETRY_CLIENT_ID("activemq_lifecycle.telemetry.client_id"),
    /**
     * Telemetry message destination to subscribe
     */
    TELEMETRY_DESTINATION("activemq_lifecycle.telemetry.destination"),
    /**
     * Consumer prefetch messages
     */
    TELEMETRY_PREFETCH_MESSAGES("activemq_lifecycle.telemetry.prefetch_messages"),
    /**
     * Message error connection client id
     */
    ERROR_CLIENT_ID("activemq_lifecycle.error.client_id"),
    /**
     * Error message destination to subscribe/produce
     */
    ERROR_DESTINATION("activemq_lifecycle.error.destination"),
    /**
     * Consumer prefetch messages
     */
    ERROR_PREFETCH_MESSAGES("activemq_lifecycle.error.prefetch_messages"),
    /**
     * Wait between reconnection attempts (milliseconds)
     */
    WAIT_BETWEEN_RECONNECT("activemq_lifecycle.connection.wait_between_reconnect"),
    /**
     * Connection timeout (milliseconds)
     */
    CONNECT_TIMEOUT("activemq_lifecycle.connection.connect_timeout"),
    /**
     * Idle timeout (seconds)
     */
    IDLE_TIMEOUT("activemq_lifecycle.connection.idle_timeout"),
    /**
     * Maximum reconnection attempt (without any success between them) before exiting JVM (negative numbers means no exit)
     */
    MAX_RECONNECTION_ATTEMPTS("activemq_lifecycle.maximum_reconnection_attempt"),
    /**
     * Exiting code when maximum reconnection attempt is reached
     */
    EXIT_CODE("activemq_lifecycle.exit_code");

    private String key;

    private ActiveMQLifecycleSettingsKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
