/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.elasticsearch.client.configuration;

import java.util.ArrayList;
import java.util.List;

public class ElasticsearchClientConfiguration {

    private String holderModuleName;
    private String providerClassName;
    private String clusterName;
    private List<ElasticsearchNode> nodes;
    private String username;
    private String password;

    private ElasticsearchClientReconnectConfiguration reconnectConfiguration;
    private ElasticsearchClientRequestConfiguration requestConfiguration;
    private ElasticsearchClientSslConfiguration sslConfiguration;

    public String getHolderModuleName() {
        return holderModuleName;
    }

    public void setHolderModuleName(String holderModuleName) {
        this.holderModuleName = holderModuleName;
    }

    public String getProviderClassName() {
        return providerClassName;
    }

    public ElasticsearchClientConfiguration setProviderClassName(String providerClassName) {
        this.providerClassName = providerClassName;
        return this;
    }

    public String getClusterName() {
        return clusterName;
    }

    public ElasticsearchClientConfiguration setClusterName(String clusterName) {
        this.clusterName = clusterName;
        return this;
    }

    public List<ElasticsearchNode> getNodes() {
        if (nodes == null) {
            nodes = new ArrayList<>();
        }

        return nodes;
    }

    public ElasticsearchClientConfiguration addNode(String address, int port) {
        getNodes().add(new ElasticsearchNode(address, port));
        return this;
    }

    public ElasticsearchClientConfiguration setNodes(List<ElasticsearchNode> nodes) {
        this.nodes = nodes;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public ElasticsearchClientConfiguration setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ElasticsearchClientConfiguration setPassword(String password) {
        this.password = password;
        return this;
    }

    public ElasticsearchClientReconnectConfiguration getReconnectConfiguration() {
        if (reconnectConfiguration == null) {
            reconnectConfiguration = new ElasticsearchClientReconnectConfiguration();
        }

        return reconnectConfiguration;
    }

    public void setReconnectConfiguration(ElasticsearchClientReconnectConfiguration reconnectConfiguration) {
        this.reconnectConfiguration = reconnectConfiguration;
    }

    public ElasticsearchClientRequestConfiguration getRequestConfiguration() {
        if (requestConfiguration == null) {
            requestConfiguration = new ElasticsearchClientRequestConfiguration();
        }

        return requestConfiguration;
    }

    public void setRequestConfiguration(ElasticsearchClientRequestConfiguration requestConfiguration) {
        this.requestConfiguration = requestConfiguration;
    }

    public ElasticsearchClientSslConfiguration getSslConfiguration() {
        if (sslConfiguration == null) {
            sslConfiguration = new ElasticsearchClientSslConfiguration();
        }

        return sslConfiguration;
    }

    public ElasticsearchClientConfiguration setSslConfiguration(ElasticsearchClientSslConfiguration sslConfiguration) {
        this.sslConfiguration = sslConfiguration;
        return this;
    }
}
