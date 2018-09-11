/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.consumer.activemq.datastore;

import org.eclipse.kapua.consumer.activemq.datastore.settings.ActiveMQDatastoreSettings;
import org.eclipse.kapua.consumer.activemq.datastore.settings.ActiveMQDatastoreSettingsKey;

public class ConnectionConfiguration {

    private String connectionHost;

    private int port;

    private String username;

    private String password;

    private int connectTimeout;

    private int maxReconnectAttempts;

    private int idelTimeout;

    public ConnectionConfiguration() {
        connectionHost = ActiveMQDatastoreSettings.getInstance().getString(ActiveMQDatastoreSettingsKey.CONNECTION_HOST);
        port = ActiveMQDatastoreSettings.getInstance().getInt(ActiveMQDatastoreSettingsKey.CONNECTION_PORT);
        username = ActiveMQDatastoreSettings.getInstance().getString(ActiveMQDatastoreSettingsKey.CONNECTION_USERNAME);
        password = ActiveMQDatastoreSettings.getInstance().getString(ActiveMQDatastoreSettingsKey.CONNECTION_PASSWORD);
        connectTimeout = ActiveMQDatastoreSettings.getInstance().getInt(ActiveMQDatastoreSettingsKey.CONNECT_TIMEOUT);
        maxReconnectAttempts = ActiveMQDatastoreSettings.getInstance().getInt(ActiveMQDatastoreSettingsKey.MAX_RECONNECTION_ATTEMPTS);
        idelTimeout = ActiveMQDatastoreSettings.getInstance().getInt(ActiveMQDatastoreSettingsKey.IDLE_TIMEOUT);
    }

    public String getConnectionHost() {
        return connectionHost;
    }

    public void setConnectionHost(String connectionHost) {
        this.connectionHost = connectionHost;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getMaxReconnectAttempts() {
        return maxReconnectAttempts;
    }

    public void setMaxReconnectAttempts(int maxReconnectAttempts) {
        this.maxReconnectAttempts = maxReconnectAttempts;
    }

    public int getIdelTimeout() {
        return idelTimeout;
    }

    public void setIdelTimeout(int idelTimeout) {
        this.idelTimeout = idelTimeout;
    }
}
