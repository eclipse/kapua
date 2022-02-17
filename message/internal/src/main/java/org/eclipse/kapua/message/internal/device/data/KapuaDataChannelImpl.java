/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
