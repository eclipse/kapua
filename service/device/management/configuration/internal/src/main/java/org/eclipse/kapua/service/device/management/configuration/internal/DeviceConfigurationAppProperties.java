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
package org.eclipse.kapua.service.device.management.configuration.internal;

import org.eclipse.kapua.service.device.management.message.KapuaAppProperties;

/**
 * /**
 * Device application configuration properties definition.
 *
 * @since 1.0
 */
public enum DeviceConfigurationAppProperties implements KapuaAppProperties {
    /**
     * Application name
     */
    APP_NAME("CONFIGURATION"),
    /**
     * Application version
     */
    APP_VERSION("1.0.0"),;

    private String value;

    DeviceConfigurationAppProperties(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

}
