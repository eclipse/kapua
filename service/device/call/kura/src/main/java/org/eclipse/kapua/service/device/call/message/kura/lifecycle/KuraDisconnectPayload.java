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
package org.eclipse.kapua.service.device.call.message.kura.lifecycle;

import org.eclipse.kapua.service.device.call.message.DevicePayload;
import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;

public class KuraDisconnectPayload extends KuraPayload implements DevicePayload
{
    private final static String UPTIME       = "uptime";
    private final static String DISPLAY_NAME = "display_name";

    public KuraDisconnectPayload() 
    {
    	super();
    }

    public KuraDisconnectPayload(String uptime, String displayName)
    {
        super();

        getMetrics().put(UPTIME, uptime);
        getMetrics().put(DISPLAY_NAME, displayName);
    }

    public String getUptime()
    {
        return (String) getMetrics().get(UPTIME);
    }

    public String getDisplayName()
    {
        return (String) getMetrics().get(DISPLAY_NAME);
    }

    public String toDisplayString()
    {
        return new StringBuilder().append("[ getUptime()=").append(getUptime())
                                  .append(", getDisplayName()=").append(getDisplayName())
                                  .append("]")
                                  .toString();
    }
}
