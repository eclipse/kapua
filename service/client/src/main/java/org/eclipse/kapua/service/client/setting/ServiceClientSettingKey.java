/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and/or its affiliates and others
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
package org.eclipse.kapua.service.client.setting;

import org.eclipse.kapua.commons.setting.SettingKey;
import org.eclipse.kapua.service.client.protocol.ProtocolDescriptor;

/**
 * Service client settings
 */
public enum ServiceClientSettingKey implements SettingKey {
    /**
     * No destination service internal use client pool - maximun total size
     */
    SERVICE_CLIENT_POOL_NO_DEST_TOTAL_MAX_SIZE("service.client_pool.no_dest_total_max_size"),
    /**
     * No destination service internal use client pool - maximun size
     */
    SERVICE_CLIENT_POOL_NO_DEST_MAX_SIZE("service.client_pool.no_dest_max_size"),
    /**
     * No destination service internal use client pool - minimum size
     */
    SERVICE_CLIENT_POOL_NO_DEST_MIN_SIZE("service.client_pool.no_dest_min_size"),
    /**
     * Allow disabling the default connector descriptor
     */
    DISABLE_DEFAULT_PROTOCOL_DESCRIPTOR("protocol.descriptor.default.disable"),
    /**
     * A URI to a configuration file for providing additional {@link ProtocolDescriptor} configurations
     */
    CONFIGURATION_URI("protocol.descriptor.configuration.uri");

    private String key;

    private ServiceClientSettingKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
