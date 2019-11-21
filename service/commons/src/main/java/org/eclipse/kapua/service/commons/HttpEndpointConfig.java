/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.commons;

public class HttpEndpointConfig {

    private String bindAddress = "0.0.0.0";
    private int port = 80;

    public String getBindAddress() {
        return bindAddress;
    }

    public void setBindAddress(String aBindAddress) {
        bindAddress = aBindAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int aPort) {
        port = aPort;
    }

    @Override
    public String toString() {
        return String.format("\"bindAddress\":\"%s\""
                + ", \"port\":\"%d\"", bindAddress, port);
    }
}
