/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.commons.message.notification;

import org.eclipse.kapua.message.internal.KapuaPayloadImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.message.notification.KapuaNotifyPayload;
import org.eclipse.kapua.service.device.management.message.notification.NotifyStatus;

/**
 * {@link KapuaNotifyPayload} implementation.
 *
 * @since 1.0.0
 */
public class KapuaNotifyPayloadImpl extends KapuaPayloadImpl implements KapuaNotifyPayload {

    private KapuaId operationId;
    private String resource;
    private NotifyStatus status;
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
    public NotifyStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(NotifyStatus status) {
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
