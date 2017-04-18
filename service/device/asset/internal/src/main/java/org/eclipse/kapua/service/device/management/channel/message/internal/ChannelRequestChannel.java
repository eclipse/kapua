/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.channel.message.internal;

import org.eclipse.kapua.service.device.management.asset.message.internal.AssetRequestChannel;
import org.eclipse.kapua.service.device.management.request.KapuaRequestChannel;

/**
 * Device asset information request channel.
 * 
 * @since 1.0
 * 
 */
public class ChannelRequestChannel extends AssetRequestChannel implements KapuaRequestChannel
{
    private String      assetName;

    /**
     * Get the asset identifier
     * 
     * @return
     */
    public String getAssetName()
    {
        return assetName;
    }

    /**
     * Set the asset identifier
     * 
     * @param assetName
     */
    public void setAssetName(String assetName)
    {
        this.assetName = assetName;
    }
}
