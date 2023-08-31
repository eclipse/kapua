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
     * Component id (used to compose unique client id to connect to various services. Could be the container id on Docker deployment.
     */
    COMPONENT_ID("component.id"),
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
