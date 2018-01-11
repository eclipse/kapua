/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.setting;

import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptor;
import org.eclipse.kapua.broker.core.router.CamelKapuaDefaultRouter;
import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Broker settings
 */
public enum BrokerSettingKey implements SettingKey {
    /**
     * Allow disabling the default connector descriptor
     */
    DISABLE_DEFAULT_CONNECTOR_DESCRIPTOR("broker.connector.descriptor.default.disable"),
    /**
     * A URI to a configuration file for providing additional {@link ConnectorDescriptor} configurations
     */
    CONFIGURATION_URI("broker.connector.descriptor.configuration.uri"),
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
     * Enable/disable the clustered stealing link feature
     */
    BROKER_STEALING_LINK_ENABLED("broker.stealing_link.enabled"),
    /**
     * Max wait time to properly startup the stealing link feature
     */
    STEALING_LINK_INITIALIZATION_MAX_WAIT_TIME("broker.stealing_link.initialization_max_wait_time"),
    /**
     * No destination broker internal use client pool - maximun total size
     */
    BROKER_CLIENT_POOL_NO_DEST_TOTAL_MAX_SIZE("broker.client_pool.no_dest_total_max_size"),
    /**
     * No destination broker internal use client pool - maximun size
     */
    BROKER_CLIENT_POOL_NO_DEST_MAX_SIZE("broker.client_pool.no_dest_max_size"),
    /**
     * No destination broker internal use client pool - minimum size
     */
    BROKER_CLIENT_POOL_NO_DEST_MIN_SIZE("broker.client_pool.no_dest_min_size"),
    /**
     * Broker name (used also for the vm connector name)
     */
    BROKER_NAME("broker.name"),
    /**
     * Camel default route configuration file name. (please specify just the name. The file path will be discovered by the class loader)
     * Used by the {@link CamelKapuaDefaultRouter} to load the routing configuration.
     */
    CAMEL_DEFAULT_ROUTE_CONFIGURATION_FILE_NAME("camel.default_route.configuration_file_name");

    private String key;

    private BrokerSettingKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
