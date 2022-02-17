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
package org.eclipse.kapua.service.scheduler.trigger.fired.quartz;

import org.eclipse.kapua.commons.model.AbstractKapuaEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTrigger;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerStatus;

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
 * {@link FiredTrigger} implementation.
 *
 * @since 1.5.0
 */
@Entity(name = "FiredTrigger")
@Table(name = "schdl_trigger_fired")
public class FiredTriggerImpl extends AbstractKapuaEntity implements FiredTrigger {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "trigger_id", nullable = false, updatable = false))
    })
    protected KapuaEid triggerId;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fired_on", nullable = false, updatable = false)
    protected Date firedOn;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, updatable = false)
    private FiredTriggerStatus status;

    @Basic
    @Column(name = "message", nullable = true, updatable = false)
    private String message;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public FiredTriggerImpl() {
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId} to set into the {@link FiredTrigger}
     * @since 1.5.0
     */
    public FiredTriggerImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Clone constructor.
     *
     * @param firedTrigger The {@link FiredTrigger} to clone.
     * @since 1.5.0
     */
    public FiredTriggerImpl(FiredTrigger firedTrigger) {
        super(firedTrigger);

        setTriggerId(firedTrigger.getTriggerId());
        setFiredOn(firedTrigger.getFiredOn());
        setStatus(firedTrigger.getStatus());
        setMessage(firedTrigger.getMessage());
    }

    @Override
    public KapuaId getTriggerId() {
        return triggerId;
    }

    @Override
    public void setTriggerId(KapuaId triggerId) {
        this.triggerId = KapuaEid.parseKapuaId(triggerId);
    }

    @Override
    public Date getFiredOn() {
        return firedOn;
    }

    @Override
    public void setFiredOn(Date firedOn) {
        this.firedOn = firedOn;
    }

    @Override
    public FiredTriggerStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(FiredTriggerStatus status) {
        this.status = status;
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
