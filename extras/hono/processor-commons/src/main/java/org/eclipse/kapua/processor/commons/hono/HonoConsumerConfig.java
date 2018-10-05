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
package org.eclipse.kapua.processor.commons.hono;

import java.util.Objects;

import org.eclipse.kapua.broker.client.hono.ClientOptions;
import org.eclipse.kapua.broker.client.hono.ClientOptions.HonoClientOptions;
import org.eclipse.kapua.commons.core.Configuration;
import org.eclipse.kapua.message.transport.TransportMessageType;

import io.vertx.proton.ProtonQoS;

public class HonoConsumerConfig {

    //hardcoded
    private boolean autoAccept;
    private String qos = ProtonQoS.AT_LEAST_ONCE.name();
    private int prefetchMessages = 10;

    //read from properties
    private String connectionHost;
    private int port;
    private String username;
    private String password;
    private String name;
    private String tenantId;
    private String truststoreFile;
    private int connectTimeout;
    private int maxReconnectAttempts;
    private long waitBetweenReconnect;
    private int idleTimeout;
    private int exitCode;
    private TransportMessageType transportMessageType;

    private HonoConsumerConfig(String prefix, Configuration config) {
        connectionHost = config.getString(prefix + ".host");
        port = config.getInteger(prefix + ".port");
        username = config.getString(prefix + ".username");
        password = config.getString(prefix + ".password");
        name = config.getString(prefix + ".name");
        tenantId = config.getString(prefix + ".tenantId");
        transportMessageType = TransportMessageType.valueOf(config.getString(prefix + ".transportMessageType"));
        truststoreFile = config.getString(prefix + ".truststoreFile");
        connectTimeout = config.getInteger(prefix + ".connectTimeout");
        maxReconnectAttempts = config.getInteger(prefix + ".maximumReconnectionAttempt");
        waitBetweenReconnect = config.getInteger(prefix + ".waitBetweenReconnect");
        idleTimeout = config.getInteger(prefix + ".idleTimeout");
        exitCode = config.getInteger(prefix + ".exitCode");
    }

    public static HonoConsumerConfig create(String prefix, Configuration config) {
        Objects.requireNonNull(prefix, "Invalid null prefix");
        Objects.requireNonNull(config, "Invalid null configuration");
        return new HonoConsumerConfig(prefix, config);
    }

    public ClientOptions createClientOptions() {
        ClientOptions options = new ClientOptions(
                getConnectionHost(),
                getPort(),
                getUsername(),
                getPassword());
        options.put(HonoClientOptions.NAME, getName());
        options.put(HonoClientOptions.TENANT_ID, getTenantId());
        options.put(HonoClientOptions.MESSAGE_TYPE, getTransportMessageType());
        options.put(HonoClientOptions.TRUSTSTORE_FILE, getTruststoreFile());
        options.put(HonoClientOptions.CONNECT_TIMEOUT, getConnectTimeout());
        options.put(HonoClientOptions.MAXIMUM_RECONNECTION_ATTEMPTS, getMaxReconnectAttempts());
        options.put(HonoClientOptions.WAIT_BETWEEN_RECONNECT, getWaitBetweenReconnect());
        options.put(HonoClientOptions.IDLE_TIMEOUT, getIdleTimeout());
        options.put(HonoClientOptions.EXIT_CODE, getExitCode());
        return options;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(int idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public TransportMessageType getTransportMessageType() {
        return transportMessageType;
    }

    public void setTransportMessageType(TransportMessageType transportMessageType) {
        this.transportMessageType = transportMessageType;
    }

    public String getTruststoreFile() {
        return truststoreFile;
    }

    public void setTruststoreFile(String truststoreFile) {
        this.truststoreFile = truststoreFile;
    }

    public long getWaitBetweenReconnect() {
        return waitBetweenReconnect;
    }

    public void setWaitBetweenReconnect(long waitBetweenReconnect) {
        this.waitBetweenReconnect = waitBetweenReconnect;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    public boolean isAutoAccept() {
        return autoAccept;
    }

    public String getQos() {
        return qos;
    }

    public int getPrefetchMessages() {
        return prefetchMessages;
    }

}
