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
package org.eclipse.kapua.service.scheduler.trigger.quartz;

import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerCreator;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * {@link TriggerCreator} implementation.
 *
 * @since 1.0.0
 */
public class TriggerCreatorImpl extends AbstractKapuaNamedEntityCreator<Trigger> implements TriggerCreator {

    private static final long serialVersionUID = -2460883485294616032L;

    private Date startsOn;
    private Date endsOn;
    private String cronScheduling;
    private Long retryInterval;
    private KapuaId triggerDefinitionId;
    private List<TriggerProperty> triggerProperties;

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId}.
     * @param name    The name.
     * @since 1.0.0
     */
    public TriggerCreatorImpl(KapuaId scopeId, String name) {
        super(scopeId, name);
    }

    @Override
    public Date getStartsOn() {
        return startsOn;
    }

    @Override
    public void setStartsOn(Date startsOn) {
        this.startsOn = startsOn;
    }

    @Override
    public Date getEndsOn() {
        return endsOn;
    }

    @Override
    public void setEndsOn(Date endsOn) {
        this.endsOn = endsOn;
    }

    @Override
    public String getCronScheduling() {
        return cronScheduling;
    }

    @Override
    public void setCronScheduling(String cronScheduling) {
        this.cronScheduling = cronScheduling;
    }

    @Override
    public Long getRetryInterval() {
        return retryInterval;
    }

    @Override
    public void setRetryInterval(Long retryInterval) {
        this.retryInterval = retryInterval;
    }

    @Override
    public KapuaId getTriggerDefinitionId() {
        return triggerDefinitionId;
    }

    @Override
    public void setTriggerDefinitionId(KapuaId triggerDefinitionId) {
        this.triggerDefinitionId = triggerDefinitionId;
    }

    @Override
    public List<TriggerProperty> getTriggerProperties() {
        if (triggerProperties == null) {
            triggerProperties = new ArrayList<>();
        }

        return triggerProperties;
    }

    @Override
    public TriggerProperty getTriggerProperty(String name) {
        return getTriggerProperties().stream().filter(tp -> tp.getName().equals(name)).findAny().orElse(null);
    }

    @Override
    public void setTriggerProperties(List<TriggerProperty> triggerProperties) {
        this.triggerProperties = triggerProperties;
    }

}
