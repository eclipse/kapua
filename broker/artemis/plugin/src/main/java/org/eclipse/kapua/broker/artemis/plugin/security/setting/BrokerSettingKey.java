/*******************************************************************************
 * Copyright (c) 2017, 2021 Red Hat Inc and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.artemis.plugin.security.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Broker settings
 */
public enum BrokerSettingKey implements SettingKey {
    /**
     * Broker host resolver implementation (if not evaluated, the default resolver will be used).
     */
    BROKER_HOST_RESOLVER_CLASS_NAME("broker.host_resolver_class_name"),
    /**
     * Broker id resolver implementation
     */
    BROKER_ID_RESOLVER_CLASS_NAME("broker.id_resolver_class_name"),
    /**
     * Broker Host
     */
    BROKER_HOST("broker.host"),
    /**
     * Broker Id
     */
    BROKER_ID("broker.id"),
    /**
     * Acceptor port for "internal use acceptor" (used by the external consumers, console and rest-api)
     * default value should be 5672 (AMQP)
     */
    INTERNAL_ACCEPTOR_PORT("broker.internal_acceptor.port"),
    /**
     * Acceptor name for "internal use acceptor" connector (used by the external consumers, console and rest-api)
     * default value should be amqp
     */
    INTERNAL_ACCEPTOR_NAME("broker.internal_acceptor.name"),
    /**
     * Broker acceptors prefix (used to read the acceptor map
     */
    ACCEPTORS("broker.acceptor"),
    /**
     * Publish message info if the message size is over the specified threshold
     */
    PUBLISHED_MESSAGE_SIZE_LOG_THRESHOLD("broker.security.published.message_size.log_threshold");

    private String key;

    private BrokerSettingKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
