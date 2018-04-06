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
package org.eclipse.kapua.processor.commons;

import java.util.Objects;

import org.eclipse.kapua.broker.client.amqp.ClientOptions;
import org.eclipse.kapua.broker.client.amqp.ClientOptions.AmqpClientOptions;
import org.eclipse.kapua.commons.core.Configuration;

import io.vertx.proton.ProtonQoS;

public class AmqpConsumerConfig {

    private String connectionHost;
    private int port;
    private String username;
    private String password;
    private int connectTimeout;
    private int maxReconnectAttempts;
    private int idelTimeout;
    private boolean autoAccept;
    private String qos;
    private String clientId;
    private String destination;
    private int prefetchMessages;

    private AmqpConsumerConfig(String aPrefix, Configuration aConfig) {
        autoAccept = false;
        qos = ProtonQoS.AT_LEAST_ONCE.name();
        connectionHost = aConfig.getString(aPrefix + ".host");
        port = aConfig.getInteger(aPrefix + ".port");
        username = aConfig.getString(aPrefix + ".username");
        password = aConfig.getString(aPrefix + ".password");
        connectTimeout = aConfig.getInteger(aPrefix + ".connectTimeout");
        maxReconnectAttempts = aConfig.getInteger(aPrefix + ".maximumReconnectionAttempt");
        idelTimeout = aConfig.getInteger(aPrefix + ".idleTimeout");
        clientId = aConfig.getString(aPrefix + ".clientId");
        destination = aConfig.getString(aPrefix + ".destination");
        prefetchMessages = aConfig.getInteger(aPrefix + ".prefetchMessages");
    }

    public static AmqpConsumerConfig create(String aPrefix, Configuration aConfig) {
        Objects.requireNonNull(aPrefix, "Invalid null prefix");
        Objects.requireNonNull(aConfig, "Invalid null configuration");
        return new AmqpConsumerConfig(aPrefix, aConfig);
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

    public boolean isAutoAccept() {
        return autoAccept;
    }

    public void setAutoAccept(boolean autoAccept) {
        this.autoAccept = autoAccept;
    }

    public String getQos() {
        return qos;
    }

    public void setQos(String qos) {
        this.qos = qos;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getPrefetchMessages() {
        return prefetchMessages;
    }

    public void setPrefetchMessages(int prefetchMessages) {
        this.prefetchMessages = prefetchMessages;
    }

    public ClientOptions createClientOptions() {
        ClientOptions options = new ClientOptions(
                this.getConnectionHost(),
                this.getPort(),
                this.getUsername(),
                this.getPassword());
        options.put(AmqpClientOptions.CLIENT_ID, getClientId());
        options.put(AmqpClientOptions.DESTINATION, getDestination());
        options.put(AmqpClientOptions.CONNECT_TIMEOUT, getConnectTimeout());
        options.put(AmqpClientOptions.MAXIMUM_RECONNECTION_ATTEMPTS, getMaxReconnectAttempts());
        options.put(AmqpClientOptions.IDLE_TIMEOUT, getIdelTimeout());
        return options;
    }
}
