/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.keystore.internal;

import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystore;
import org.eclipse.kapua.service.device.management.message.KapuaAppProperties;

/**
 * {@link DeviceKeystore} {@link KapuaAppProperties} implementation.
 *
 * @since 1.5.0
 */
public enum DeviceKeystoreAppProperties implements KapuaAppProperties {
    /**
     * Application name
     */
    APP_NAME("KEYS"),
    /**
     * Version
     */
    APP_VERSION("1.0.0"),
    ;

    private final String value;

    DeviceKeystoreAppProperties(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

}
