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
package org.eclipse.kapua.message.internal.device.lifecycle;

import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectPayload;
import org.eclipse.kapua.message.internal.KapuaPayloadImpl;

/**
 * Kapua data message payload object reference implementation.
 * 
 * @since 1.0
 *
 */
public class KapuaDisconnectPayloadImpl extends KapuaPayloadImpl implements KapuaDisconnectPayload {
	
	private String uptime;
    private String displayName;

    /**
     * Constructor
     * 
     * @param uptime
     * @param displayName
     */
    public KapuaDisconnectPayloadImpl(String uptime,
    		String displayName) {
    	this.uptime = uptime;
    	this.displayName = displayName;
    }
    
    @Override
    public String toDisplayString()
    {
        return new StringBuilder().append("[ getUptime()=").append(getUptime())
                                  .append(", getDisplayName()=").append(getDisplayName())
                                  .append("]")
                                  .toString();
    }

    @Override
	public String getUptime() {
		return uptime;
	}

    @Override
	public String getDisplayName() {
		return displayName;
	}

}
