/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.channel.message.internal;

import org.eclipse.kapua.service.device.management.asset.message.internal.AssetRequestChannel;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestChannel;

/**
 * Device asset information request channel.
 *
 * @since 1.0
 */
public class ChannelRequestChannel extends AssetRequestChannel implements KapuaRequestChannel {

    private String assetName;

    /**
     * Get the asset identifier
     *
     * @return
     */
    public String getAssetName() {
        return assetName;
    }

    /**
     * Set the asset identifier
     *
     * @param assetName
     */
    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }
}
