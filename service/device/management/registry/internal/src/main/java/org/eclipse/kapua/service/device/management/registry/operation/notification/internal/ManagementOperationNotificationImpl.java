/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.registry.operation.notification.internal;

import org.eclipse.kapua.commons.model.AbstractKapuaEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.message.notification.OperationStatus;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotification;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity(name = "DeviceManagementOperationNotification")
@Table(name = "dvcm_device_management_operation_notification")
public class ManagementOperationNotificationImpl extends AbstractKapuaEntity implements ManagementOperationNotification {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "operation_id", nullable = false, updatable = false))
    })
    private KapuaEid operationId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent_on", nullable = false, updatable = false)
    private Date sentOn;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, updatable = false)
    private OperationStatus status;

    @Basic
    @Column(name = "resource", nullable = false, updatable = false)
    private String resource;

    @Basic
    @Column(name = "progress", nullable = false, updatable = false)
    private Integer progress;

    @Basic
    @Column(name = "message", nullable = true, updatable = false)
    private String message;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public ManagementOperationNotificationImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId}.
     * @since 1.0.0
     */
    public ManagementOperationNotificationImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Clone constructor.
     *
     * @param managementOperationNotification The {@link ManagementOperationNotification} to clone.
     * @since 1.1.0
     */
    public ManagementOperationNotificationImpl(ManagementOperationNotification managementOperationNotification) {
        super(managementOperationNotification);

        setOperationId(managementOperationNotification.getOperationId());
        setSentOn(managementOperationNotification.getSentOn());
        setStatus(managementOperationNotification.getStatus());
        setResource(managementOperationNotification.getResource());
        setProgress(managementOperationNotification.getProgress());
        setMessage(managementOperationNotification.getMessage());
    }

    @Override
    public KapuaId getOperationId() {
        return operationId;
    }

    @Override
    public void setOperationId(KapuaId operationId) {
        this.operationId = KapuaEid.parseKapuaId(operationId);
    }

    @Override
    public Date getSentOn() {
        return sentOn;
    }

    @Override
    public void setSentOn(Date sentOn) {
        this.sentOn = sentOn;
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
    public String getResource() {
        return resource;
    }

    @Override
    public void setResource(String resource) {
        this.resource = resource;
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
