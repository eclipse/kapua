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
package org.eclipse.kapua.service.scheduler.trigger.quartz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerCreator;
import org.eclipse.kapua.service.scheduler.trigger.TriggerProperty;

/**
 * Trigger creator service implementation.
 * 
 * @since 1.0
 * 
 */
public class TriggerCreatorImpl extends AbstractKapuaNamedEntityCreator<Trigger> implements TriggerCreator {

    private static final long serialVersionUID = -2460883485294616032L;

    private Date startsOn;
    private Date endsOn;
    private String cronScheduling;
    private Long retryInterval;
    private List<TriggerProperty> triggerProperties;

    /**
     * Constructor
     * 
     * @param scopeId
     * @param name
     *            trigger name
     */
    public TriggerCreatorImpl(KapuaId scopeId, String name) {
        super(scopeId, name);
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

    @Override
    public List<TriggerProperty> getTriggerProperties() {
        if (triggerProperties == null) {
            triggerProperties = new ArrayList<>();
        }

        return triggerProperties;
    }

    @Override
    public void setTriggerProperties(List<TriggerProperty> triggerProperties) {
        this.triggerProperties = triggerProperties;
    }

}
