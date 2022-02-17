/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.scheduler.trigger.definition.quartz;

import com.google.common.collect.Lists;
import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinition;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerProperty;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerType;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link TriggerDefinition} implementation.
 *
 * @since 1.1.0
 */
@Entity(name = "TriggerDefinition")
@Table(name = "schdl_trigger_definition")
public class TriggerDefinitionImpl extends AbstractKapuaNamedEntity implements TriggerDefinition {

    private static final long serialVersionUID = 3747451706859757246L;

    @Enumerated(EnumType.STRING)
    @Column(name = "trigger_type", nullable = false, updatable = false)
    private TriggerType triggerType;

    @Basic
    @Column(name = "processor_name", nullable = false, updatable = false)
    private String processorName;

    @ElementCollection
    @CollectionTable(name = "schdl_trigger_definition_properties", joinColumns = @JoinColumn(name = "trigger_definition_id", referencedColumnName = "id"))
    private List<TriggerPropertyImpl> triggerProperties;

    /**
     * Constructor.
     *
     * @since 1.1.0
     */
    public TriggerDefinitionImpl() {
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId} to set into the {@link TriggerDefinition}
     * @since 1.1.0
     */
    public TriggerDefinitionImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Clone constructor.
     *
     * @param triggerDefinition The {@link TriggerDefinition} to clone
     * @since 1.1.0
     */
    public TriggerDefinitionImpl(TriggerDefinition triggerDefinition) {
        super(triggerDefinition);

        setTriggerType(triggerDefinition.getTriggerType());
        setProcessorName(triggerDefinition.getProcessorName());
        setTriggerProperties(triggerDefinition.getTriggerProperties());
    }

    @Override
    public TriggerType getTriggerType() {
        return triggerType;
    }

    @Override
    public void setTriggerType(TriggerType triggerType) {
        this.triggerType = triggerType;

    }

    @Override
    public String getProcessorName() {
        return processorName;
    }

    @Override
    public void setProcessorName(String processorName) {
        this.processorName = processorName;
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

        for (TriggerProperty sp : triggerProperties) {
            this.triggerProperties.add(TriggerPropertyImpl.parse(sp));
        }
    }
}
