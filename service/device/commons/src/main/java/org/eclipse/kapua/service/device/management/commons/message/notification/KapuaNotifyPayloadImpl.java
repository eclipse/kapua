/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.commons.message.notification;

import org.eclipse.kapua.message.internal.KapuaPayloadImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.message.notification.KapuaNotifyPayload;
import org.eclipse.kapua.service.device.management.message.notification.OperationStatus;

/**
 * {@link KapuaNotifyPayload} implementation.
 *
 * @since 1.0.0
 */
public class KapuaNotifyPayloadImpl extends KapuaPayloadImpl implements KapuaNotifyPayload {

    private KapuaId operationId;
    private String resource;
    private OperationStatus status;
    private Integer progress;
    private String message;

    @Override
    public KapuaId getOperationId() {
        return operationId;
    }

    @Override
    public void setOperationId(KapuaId operationId) {
        this.operationId = operationId;
    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public void setResource(String resource) {
        this.resource = resource;
    }

    @Override
    public OperationStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(OperationStatus status) {
        this.status = status;
    }

    @Override
    public Integer getProgress() {
        return progress;
    }

    @Override
    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
