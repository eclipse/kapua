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

import com.google.common.collect.Lists;
import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerProperty;
import org.eclipse.kapua.service.scheduler.trigger.definition.quartz.TriggerPropertyImpl;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * {@link Trigger} implementation.
 *
 * @since 1.0.0
 */
@Entity(name = "Trigger")
@Table(name = "schdl_trigger")
public class TriggerImpl extends AbstractKapuaNamedEntity implements Trigger {

    private static final long serialVersionUID = 3250140890001324842L;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "starts_on", nullable = true, updatable = true)
    private Date startsOn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ends_on", nullable = true, updatable = true)
    private Date endsOn;

    @Basic
    @Column(name = "cron_scheduling", nullable = true, updatable = true)
    private String cronScheduling;

    @Basic
    @Column(name = "retry_interval", nullable = true, updatable = true)
    private Long retryInterval;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "trigger_definition_id"))
    })
    private KapuaEid triggerDefinitionId;

    @ElementCollection
    @CollectionTable(name = "schdl_trigger_properties", joinColumns = @JoinColumn(name = "trigger_id", referencedColumnName = "id"))
    private List<TriggerPropertyImpl> triggerProperties;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    protected TriggerImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId} to set into the {@link Trigger}
     * @since 1.0.0
     */
    public TriggerImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Clone constructor.
     *
     * @param trigger The {@link Trigger} to clone.
     * @since 1.1.0
     */
    public TriggerImpl(Trigger trigger) {
        super(trigger);

        setStartsOn(trigger.getStartsOn());
        setEndsOn(trigger.getEndsOn());
        setCronScheduling(trigger.getCronScheduling());
        setRetryInterval(trigger.getRetryInterval());
        setTriggerDefinitionId(trigger.getTriggerDefinitionId());
        setTriggerProperties(trigger.getTriggerProperties());
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
        this.triggerDefinitionId = KapuaEid.parseKapuaId(triggerDefinitionId);
    }

    @Override
    public List<TriggerProperty> getTriggerProperties() {
        if (triggerProperties == null) {
            triggerProperties = new ArrayList<>();
        }

        return Lists.newArrayList(triggerProperties);
    }

    @Override
    public TriggerProperty getTriggerProperty(String name) {
        return getTriggerProperties().stream().filter(tp -> tp.getName().equals(name)).findAny().orElse(null);
    }

    @Override
    public void setTriggerProperties(List<TriggerProperty> triggerProperties) {
        this.triggerProperties = new ArrayList<>();

        triggerProperties.forEach((tp) -> this.triggerProperties.add(TriggerPropertyImpl.parse(tp)));
    }

}
