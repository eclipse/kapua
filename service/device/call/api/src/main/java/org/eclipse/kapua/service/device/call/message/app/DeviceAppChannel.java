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
package org.eclipse.kapua.service.device.call.message.app;

import org.eclipse.kapua.service.device.call.message.DeviceChannel;

/**
 * Device application channel definition.
 * 
 * @since 1.0
 *
 */
public interface DeviceAppChannel extends DeviceChannel
{

    /**
     * Get the application identifier
     * 
     * @return
     */
    public String getAppId();

    /**
     * Set the application identifier
     * 
     * @param appId
     */
    public void setAppId(String appId);

}
