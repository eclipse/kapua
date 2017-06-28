/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.service.event.internal;

import java.util.Date;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.event.KapuaEvent;

public class KapuaEventImpl implements KapuaEvent {

    private String contextId;
    private Date timestamp;
    private KapuaId userId;
    private String service;
    private String entityType;
    private KapuaId entityId;
    private String operation;
    private OperationStatus operationStatus;
    private String failureMessage;
    private String inputs;
    private String outputs;
    private String properties;
    private String note;
    
    @Override
    public String getContextId() {
        return contextId;
    }
    
    public void setContextId(String contextId) {
        this.contextId = contextId;
    }
    
    @Override
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public KapuaId getUserId() {
        return userId;
    }
    
    public void setUserId(KapuaId userId) {
        this.userId = userId;
    }
    
    @Override
    public String getService() {
        return service;
    }
    
    public void setService(String service) {
        this.service = service;
    }
    
    @Override
    public String getEntityType() {
        return entityType;
    }
    
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
    
    @Override
    public KapuaId getEntityId() {
        return entityId;
    }
    
    public void setEntityId(KapuaId entityId) {
        this.entityId = entityId;
    }
    
    @Override
    public String getOperation() {
        return operation;
    }
    
    public void setOperation(String operation) {
        this.operation = operation;
    }
    
    @Override
    public OperationStatus getOperationStatus() {
        return operationStatus;
    }
    
    public void setOperationStatus(OperationStatus operationStatus) {
        this.operationStatus = operationStatus;
    }
    
    @Override
    public String getFailureMessage() {
        return failureMessage;
    }
    
    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }
    
    @Override
    public String getInputs() {
        return inputs;
    }
    
    public void setInputs(String inputs) {
        this.inputs = inputs;
    }
    
    @Override
    public String getOutputs() {
        return outputs;
    }
    
    public void setOutputs(String outputs) {
        this.outputs = outputs;
    }
    
    @Override
    public String getProperties() {
        return properties;
    }
    
    public void setProperties(String properties) {
        this.properties = properties;
    }
    
    @Override
    public String getNote() {
        return note;
    }
    
    public void setNote(String note) {
        this.note = note;
    }
}