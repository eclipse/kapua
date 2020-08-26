/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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
 * {@link KapuaDataChannel} implementation.
 *
 * @since 1.0.0
 */
public class KapuaDataChannelImpl extends KapuaChannelImpl implements KapuaDataChannel {

    private String clientId;

    /**
     * Gets the client identifier
     *
     * @return
     * @since 1.0.0
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Sets the client identifier
     *
     * @param clientId
     * @since 1.0.0
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

}
