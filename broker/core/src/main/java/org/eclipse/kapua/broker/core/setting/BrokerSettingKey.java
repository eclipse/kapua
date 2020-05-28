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
package org.eclipse.kapua.broker.core.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Broker settings
 */
public enum BrokerSettingKey implements SettingKey {
    /**
     * Broker IP resolver implementation (if not evaluated, the default resolver will be used).
     */
    BROKER_IP_RESOLVER_CLASS_NAME("broker.ip_resolver_class_name"),
    /**
     * Broker id resolver implementation
     */
    BROKER_ID_RESOLVER_CLASS_NAME("broker.id_resolver_class_name"),
    /**
     * Broker IP used by the default resolver.
     */
    BROKER_IP("broker.ip"),
    /**
     * System message creator implementation.
     */
    SYSTEM_MESSAGE_CREATOR_CLASS_NAME("broker.system.message_creator_class_name"),
    /**
     * Authenticator implementation
     */
    AUTHENTICATOR_CLASS_NAME("broker.authenticator_class_name"),
    /**
     * Admin authentication logic custom implementation
     */
    ADMIN_AUTHENTICATION_LOGIC_CLASS_NAME("broker.authentication_logic_admin_class_name"),
    /**
     * User authentication logic custom implementation
     */
    USER_AUTHENTICATION_LOGIC_CLASS_NAME("broker.authentication_logic_user_class_name"),
    /**
     * Authorizer implementation
     */
    AUTHORIZER_CLASS_NAME("broker.authorizer_class_name"),
    /**
     * Enable/disable the clustered stealing link feature
     */
    BROKER_STEALING_LINK_ENABLED("broker.stealing_link.enabled"),
    /**
     * Max wait time to properly startup the stealing link feature
     */
    STEALING_LINK_INITIALIZATION_MAX_WAIT_TIME("broker.stealing_link.initialization_max_wait_time"),
    /**
     * Publish message info if the message size is over the specified threshold
     */
    PUBLISHED_MESSAGE_SIZE_LOG_THRESHOLD("broker.security.published.message_size.log_threshold"),
    /**
     * Jaxb context provider class name
     */
    JAXB_CONTEXT_CLASS_NAME("broker.jaxb_context_class_name");

    private String key;

    private BrokerSettingKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
