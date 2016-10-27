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
package org.eclipse.kapua.service.device.call.message.app.kura;

import org.eclipse.kapua.service.device.call.message.app.DeviceAppChannel;
import org.eclipse.kapua.service.device.call.message.kura.KuraChannel;

/**
 * Kura application message channel.
 * 
 * @since 1.0
 *
 */
public abstract class KuraAppChannel extends KuraChannel implements DeviceAppChannel
{
    protected String appId;

    /**
     * Constructor
     */
    public KuraAppChannel()
    {
        super();
    }

    /**
     * Constructor
     * 
     * @param scopeNamespace
     * @param clientId
     */
    public KuraAppChannel(String scopeNamespace, String clientId)
    {
        this(null, scopeNamespace, clientId);
    }

    /**
     * Constructor
     * 
     * @param controlDestinationPrefix
     * @param scopeNamespace
     * @param clientId
     */
    public KuraAppChannel(String controlDestinationPrefix, String scopeNamespace, String clientId)
    {
        super(controlDestinationPrefix, scopeNamespace, clientId);
    }

    @Override
    public String getAppId()
    {
        return appId;
    }

    @Override
    public void setAppId(String appId)
    {
        this.appId = appId;
    }
}
