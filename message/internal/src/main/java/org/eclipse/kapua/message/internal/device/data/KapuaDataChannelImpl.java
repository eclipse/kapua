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
package org.eclipse.kapua.message.internal.device.data;

import org.eclipse.kapua.message.device.data.KapuaDataChannel;
import org.eclipse.kapua.message.internal.KapuaChannelImpl;

/**
 * Kapua data message channel object reference implementation.
 * 
 * @since 1.0
 *
 */
public class KapuaDataChannelImpl extends KapuaChannelImpl implements KapuaDataChannel {

    private String clientId;

    /**
     * Get the client identifier
     * 
     * @return
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Set the client identifier
     * 
     * @param clientId
     */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
}
