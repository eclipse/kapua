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
package org.eclipse.kapua.service.device.management.asset.message.internal;

import org.eclipse.kapua.service.device.management.asset.internal.DeviceAssetAppProperties;
import org.eclipse.kapua.service.device.management.commons.message.KapuaAppChannelImpl;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseChannel;

/**
 * Asset {@link KapuaResponseChannel}.
 *
 * @since 1.0.0
 */
public class AssetResponseChannel extends KapuaAppChannelImpl implements KapuaResponseChannel {

    /**
     * Constructor
     *
     * @since 1.5.0
     */
    public AssetResponseChannel() {
        setAppName(DeviceAssetAppProperties.APP_NAME);
        setVersion(DeviceAssetAppProperties.APP_VERSION);
    }
}
