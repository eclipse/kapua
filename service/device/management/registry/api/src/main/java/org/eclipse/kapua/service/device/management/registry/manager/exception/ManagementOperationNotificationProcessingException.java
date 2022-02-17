/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.registry.manager.exception;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.message.notification.NotifyStatus;

import java.util.Date;

public class ManagementOperationNotificationProcessingException extends KapuaException {

    private KapuaId scopeId;
    private KapuaId operationId;
    private NotifyStatus status;
    private Date updatedOn;
    private Integer progress;

    public ManagementOperationNotificationProcessingException(KapuaException ke, KapuaId scopeId, KapuaId operationId, NotifyStatus status, Date updateOn, Integer progress) {
        super(ManagementOperationManagerErrorCodes.MANAGEMENT_NOTIFICATION_PROCESSING_ERROR, ke, scopeId, operationId, status, updateOn, progress);

        setScopeId(scopeId);
        setOperationId(operationId);
        setStatus(status);
        setUpdatedOn(updateOn);
        setProgress(progress);
    }

    public KapuaId getScopeId() {
        return scopeId;
    }

    private void setScopeId(KapuaId scopeId) {
        this.scopeId = scopeId;
    }

    public KapuaId getOperationId() {
        return operationId;
    }

    private void setOperationId(KapuaId operationId) {
        this.operationId = operationId;
    }

    public NotifyStatus getStatus() {
        return status;
    }

    private void setStatus(NotifyStatus status) {
        this.status = status;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    private void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Integer getProgress() {
        return progress;
    }

    private void setProgress(Integer progress) {
        this.progress = progress;
    }
}
