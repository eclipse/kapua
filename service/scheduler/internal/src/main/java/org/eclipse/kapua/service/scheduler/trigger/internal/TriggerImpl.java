/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.scheduler.trigger.internal;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;

/**
 * Trigger entity implementation.
 *
 * @since 1.0
 */
@Entity(name = "Trigger")
@Table(name = "schdl_trigger")
public class TriggerImpl extends AbstractKapuaNamedEntity implements Trigger {

    private static final long serialVersionUID = 3250140890001324842L;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "starts_on", nullable = true, updatable = false)
    private Date startsOn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ends_on", nullable = true, updatable = false)
    private Date endsOn;

    @Basic
    @Column(name = "cron_scheduling", nullable = true, updatable = false)
    private String cronScheduling;

    @Basic
    @Column(name = "retry_interval", nullable = true, updatable = false)
    private Long retryInterval;

    /**
     * Constructor
     */
    protected TriggerImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param scopeId
     */
    public TriggerImpl(KapuaId scopeId) {
        super(scopeId);
    }

    public Date getStartsOn() {
        return startsOn;
    }

    public void setStartsOn(Date startsOn) {
        this.startsOn = startsOn;
    }

    public Date getEndsOn() {
        return endsOn;
    }

    public void setEndsOn(Date endsOn) {
        this.endsOn = endsOn;
    }

    public String getCronScheduling() {
        return cronScheduling;
    }

    public void setCronScheduling(String cronScheduling) {
        this.cronScheduling = cronScheduling;
    }

    public Long getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(Long retryInterval) {
        this.retryInterval = retryInterval;
    }
}
