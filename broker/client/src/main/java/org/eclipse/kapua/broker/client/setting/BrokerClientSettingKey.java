/*******************************************************************************
 * Copyright (c) 2017, 2021 Red Hat Inc and/or its affiliates and others
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
package org.eclipse.kapua.broker.client.setting;

import org.eclipse.kapua.broker.client.protocol.ProtocolDescriptor;
import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Broker client settings
 */
public enum BrokerClientSettingKey implements SettingKey {
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
     * Allow disabling the default connector descriptor
     */
    DISABLE_DEFAULT_PROTOCOL_DESCRIPTOR("protocol_descriptor.default.disable"),
    /**
     * A URI to a configuration file for providing additional {@link ProtocolDescriptor} configurations
     */
    CONFIGURATION_URI("protocol_descriptor.configuration.uri");

    private String key;

    private BrokerClientSettingKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
