/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.client.security.bean;

import java.security.cert.Certificate;

/**
 * Utility class to move infos between authentication layers
 *
 */
public class ConnectionInfo {

    private String connectionId;
    private String clientId;
    private String connectorName;
    private String transportProtocol;
    private String clientIp;
    private Boolean sslEnabled;
    private Certificate[] certificates;

    public ConnectionInfo(String connectionId, String clientId, String clientIp, String connectorName, String transportProtocol, String sslEnabledStr, Certificate[] clientCertificates) {
        this.connectionId = connectionId;
        this.clientId = clientId;
        this.connectorName = connectorName;
        this.transportProtocol = transportProtocol;
        this.clientIp = clientIp;
        certificates = clientCertificates;
        sslEnabled = (sslEnabledStr == null) ? null : Boolean.valueOf(sslEnabledStr);
    }

    public String getConnectionId() {
        return connectionId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getConnectorName() {
        return connectorName;
    }

    public String getTransportProtocol() {
        return transportProtocol;
    }

    public String getClientIp() {
        return clientIp;
    }

    public Certificate[] getCertificates() {
        return certificates;
    }

    public Boolean getSslEnabled() {
        return sslEnabled;
    }
}
