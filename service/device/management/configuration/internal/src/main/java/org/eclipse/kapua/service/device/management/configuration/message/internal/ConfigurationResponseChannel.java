/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.configuration.message.internal;

import org.eclipse.kapua.service.device.management.commons.message.KapuaAppChannelImpl;
import org.eclipse.kapua.service.device.management.configuration.internal.DeviceConfigurationAppProperties;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseChannel;

/**
 * Configuration {@link KapuaResponseChannel}.
 *
 * @since 1.0.0
 */
public class ConfigurationResponseChannel extends KapuaAppChannelImpl implements KapuaResponseChannel {

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public ConfigurationResponseChannel() {
        setAppName(DeviceConfigurationAppProperties.APP_NAME);
        setVersion(DeviceConfigurationAppProperties.APP_VERSION);
    }
}
