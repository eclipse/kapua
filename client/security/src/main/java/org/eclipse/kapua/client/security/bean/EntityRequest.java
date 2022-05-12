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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EntityRequest implements Request {

    protected static Logger logger = LoggerFactory.getLogger(AuthResponse.class);

    @JsonProperty("requester")
    private String requester;

    @JsonProperty("clusterName")
    private String clusterName;

    @JsonProperty("action")
    private String action;

    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("entity")
    private String entity;

    @JsonProperty("name")
    private String name;

    public EntityRequest() {
    }

    public EntityRequest(String clusterName, String requester, String action, String entity, String name) {
        this.clusterName = clusterName;
        this.requester = requester;
        this.action = action;
        this.entity = entity;
        this.name = name;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
