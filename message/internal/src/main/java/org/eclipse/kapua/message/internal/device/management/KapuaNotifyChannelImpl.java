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
package org.eclipse.kapua.message.internal.device.management;

import org.eclipse.kapua.message.device.lifecycle.KapuaNotifyChannel;
import org.eclipse.kapua.message.internal.KapuaChannelImpl;

/**
 * Kapua notify message channel object reference implementation.
 *
 * @since 1.0
 */
public class KapuaNotifyChannelImpl extends KapuaChannelImpl implements KapuaNotifyChannel {

    private String clientId;
    private String resources;

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getResources() {
        return resources;
    }

    @Override
    public void setResources(String resources) {
        this.resources = resources;
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("Client id '");
        strBuilder.append(clientId);
        strBuilder.append("' - semantic topic '");
        strBuilder.append(super.toString());
        strBuilder.append("'");
        return strBuilder.toString();
    }

}
