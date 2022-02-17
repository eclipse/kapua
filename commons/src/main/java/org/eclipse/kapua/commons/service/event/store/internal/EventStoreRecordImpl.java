/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.service.event.store.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecord;
import org.eclipse.kapua.event.ServiceEvent.EventStatus;
import org.eclipse.kapua.model.id.KapuaId;

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

/**
 * {@link EventStoreRecord} implementation
 *
 * @since 1.0.0
 */
@Entity(name = "EventStoreRecord")
@Table(name = "sys_event_store")
public class EventStoreRecordImpl extends AbstractKapuaUpdatableEntity implements EventStoreRecord {

    private static final long serialVersionUID = -2416000835110726619L;

    @Basic
    @Column(name = "context_id", nullable = false, updatable = false)
    private String contextId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "event_on", nullable = false, updatable = false)
    private Date timestamp;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "user_id"))
    })
    private KapuaEid userId;

    @Basic
    @Column(name = "service", nullable = false, updatable = false)
    private String service;

    @Basic
    @Column(name = "entity_type", nullable = false, updatable = false)
    private String entityType;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "entity_id"))
    })
    private KapuaEid entityId;

    @Basic
    @Column(name = "operation", nullable = false, updatable = false)
    private String operation;

    @Basic
    @Column(name = "inputs", nullable = false, updatable = false)
    private String inputs;

    @Basic
    @Column(name = "outputs", nullable = true, updatable = true)
    private String outputs;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = true, updatable = true)
    private EventStatus status;

    @Basic
    @Column(name = "note", nullable = true, updatable = true)
    private String note;

    public EventStoreRecordImpl() {
        status = EventStatus.TRIGGERED;
    }

    /**
     * Constructor.
     *
     * @param scopeId
     * @since 1.0.0
     */
    public EventStoreRecordImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Clone constructor
     *
     * @throws KapuaException
     * @since 1.1.0
     */
    public EventStoreRecordImpl(EventStoreRecord eventStoreRecord) throws KapuaException {
        super(eventStoreRecord);

        setContextId(eventStoreRecord.getContextId());
        setTimestamp(eventStoreRecord.getTimestamp());
        setUserId(eventStoreRecord.getUserId());
        setService(eventStoreRecord.getService());
        setEntityType(eventStoreRecord.getEntityType());
        setEntityId(eventStoreRecord.getEntityId());
        setOperation(eventStoreRecord.getOperation());
        setInputs(eventStoreRecord.getInputs());
        setOutputs(eventStoreRecord.getOutputs());
        setStatus(eventStoreRecord.getStatus());
        setNote(eventStoreRecord.getNote());
    }

    @Override
    public String getContextId() {
        return contextId;
    }

    @Override
    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public KapuaId getUserId() {
        return userId;
    }

    @Override
    public void setUserId(KapuaId userId) {
        this.userId = KapuaEid.parseKapuaId(userId);
    }

    @Override
    public String getService() {
        return service;
    }

    @Override
    public void setService(String service) {
        this.service = service;
    }

    @Override
    public String getEntityType() {
        return entityType;
    }

    @Override
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    @Override
    public KapuaId getScopeId() {
        return scopeId;
    }

    @Override
    public void setScopeId(KapuaId scopeId) {
        this.scopeId = KapuaEid.parseKapuaId(scopeId);
    }

    @Override
    public KapuaId getEntityId() {
        return entityId;
    }

    @Override
    public void setEntityId(KapuaId entityId) {
        this.entityId = KapuaEid.parseKapuaId(entityId);
    }

    @Override
    public String getOperation() {
        return operation;
    }

    @Override
    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String getInputs() {
        return inputs;
    }

    @Override
    public void setInputs(String inputs) {
        this.inputs = inputs;
    }

    @Override
    public String getOutputs() {
        return outputs;
    }

    @Override
    public void setOutputs(String outputs) {
        this.outputs = outputs;
    }

    @Override
    public EventStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(EventStatus status) {
        this.status = status;
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public void setNote(String note) {
        this.note = note;
    }
}
