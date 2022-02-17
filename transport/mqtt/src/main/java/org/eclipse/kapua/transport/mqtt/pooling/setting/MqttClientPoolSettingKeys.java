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
package org.eclipse.kapua.transport.mqtt.pooling.setting;

import org.eclipse.kapua.commons.setting.SettingKey;
import org.eclipse.kapua.transport.mqtt.MqttClient;

/**
 * Available settings key for MQTT client pool for MQTT transport level
 *
 * @since 1.0.0
 */
public enum MqttClientPoolSettingKeys implements SettingKey {

    /**
     * The prefix for the id set to the {@link MqttClient}
     *
     * @since 1.0.0
     */
    CLIENT_POOL_CLIENT_ID_PREFIX("client.pool.client.id.prefix"),

    /**
     * The minimum size for the client pool.
     *
     * @since 1.0.0
     */
    CLIENT_POOL_SIZE_IDLE_MIN("client.pool.size.idle.min"),

    /**
     * The maximum size for the client pool.
     *
     * @since 1.0.0
     */
    CLIENT_POOL_SIZE_IDLE_MAX("client.pool.size.idle.max"),

    /**
     * FIXME [javadoc] document property
     *
     * @since 1.0.0
     */
    CLIENT_POOL_SIZE_TOTAL_MAX("client.pool.size.total.max"),

    /**
     * The max time to wait an available {@link MqttClient}
     *
     * @since 1.0.0
     */
    CLIENT_POOL_BORROW_WAIT_MAX("client.pool.borrow.wait.max"),

    /**
     * The time interval between each eviction on the client pool
     *
     * @since 1.0.0
     */
    CLIENT_POOL_EVICTION_INTERVAL("client.pool.eviction.interval"),

    /**
     * FIXME [javadoc] document property
     *
     * @since 1.0.0
     */
    CLIENT_POOL_WHEN_EXAUSTED_BLOCK("client.pool.when.exhausted.block"),

    /**
     * FIXME [javadoc] document property
     *
     * @since 1.0.0
     */
    CLIENT_POOL_WHEN_IDLE_TEST("client.pool.when.idle.test"),

    /**
     * Checks client status when borrowing client from the pool.
     *
     * @since 1.0.0
     */
    CLIENT_POOL_ON_BORROW_TEST("client.pool.on.borrow.test"),

    /**
     * Checks client status when returning client to the pool.
     *
     * @since 1.0.0
     */
    CLIENT_POOL_ON_RETURN_TEST("client.pool.on.return.test"),
    ;

    /**
     * The key value in the configuration resources.
     *
     * @since 1.0.0
     */
    private String key;

    /**
     * Set up the {@code enum} with the key value provided
     *
     * @param key
     *            The value mapped by this {@link Enum} value
     * @since 1.0.0
     */
    private MqttClientPoolSettingKeys(String key) {
        this.key = key;
    }

    /**
     * Gets the key for this {@link MqttClientPoolSettingKeys}
     *
     * @since 1.0.0
     */
    public String key() {
        return key;
    }
}
