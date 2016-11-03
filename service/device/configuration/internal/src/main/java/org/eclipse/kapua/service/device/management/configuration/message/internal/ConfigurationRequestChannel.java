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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.configuration.message.internal;

import org.eclipse.kapua.service.device.management.KapuaMethod;
import org.eclipse.kapua.service.device.management.commons.message.response.KapuaAppChannelImpl;
import org.eclipse.kapua.service.device.management.request.KapuaRequestChannel;

/**
 * Device configuration request channel.
 * 
 * @since 1.0
 * 
 */
public class ConfigurationRequestChannel extends KapuaAppChannelImpl implements KapuaRequestChannel
{
    private KapuaMethod method;
    private String      configurationId;
    private String      componentId;

    @Override
    public KapuaMethod getMethod()
    {
        return method;
    }

    @Override
    public void setMethod(KapuaMethod method)
    {
        this.method = method;
    }

    /**
     * Get the device configuration identifier
     * 
     * @return
     */
    public String getConfigurationId()
    {
        return configurationId;
    }

    /**
     * Set the device configuration identifier
     * 
     * @param configurationId
     */
    public void setConfigurationId(String configurationId)
    {
        this.configurationId = configurationId;
    }

    /**
     * Get the device configuration component identifier
     * 
     * @return
     */
    public String getComponentId()
    {
        return componentId;
    }

    /**
     * Set the device configuration component identifier
     * 
     * @param componentId
     */
    public void setComponentId(String componentId)
    {
        this.componentId = componentId;
    }
}
