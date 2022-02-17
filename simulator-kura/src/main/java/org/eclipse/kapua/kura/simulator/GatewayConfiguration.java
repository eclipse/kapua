/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
