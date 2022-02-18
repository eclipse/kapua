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
package org.eclipse.kapua.service.device.management.asset.internal;

import org.eclipse.kapua.service.device.management.asset.DeviceAsset;
import org.eclipse.kapua.service.device.management.message.KapuaAppProperties;

/**
 * {@link DeviceAsset} {@link KapuaAppProperties} implementation
 *
 * @since 1.0.0
 */
public enum DeviceAssetAppProperties implements KapuaAppProperties {

    /**
     * Application name
     */
    APP_NAME("ASSET"),
    /**
     * Version
     */
    APP_VERSION("1.0.0"),
    ;

    private final String value;

    DeviceAssetAppProperties(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

}
