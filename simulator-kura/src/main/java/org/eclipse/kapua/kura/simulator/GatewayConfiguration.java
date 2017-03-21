/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator;

/**
 * This holds the main configuration of a gateway instance
 */
public class GatewayConfiguration {

    private final String brokerUrl;
    private final String accountName;
    private final String clientId;

    public GatewayConfiguration(final String brokerUrl, final String accountName, final String clientId) {
        this.brokerUrl = brokerUrl;
        this.accountName = accountName;
        this.clientId = clientId;
    }

    public String getBrokerUrl() {
        return brokerUrl;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getClientId() {
        return clientId;
    }
}
