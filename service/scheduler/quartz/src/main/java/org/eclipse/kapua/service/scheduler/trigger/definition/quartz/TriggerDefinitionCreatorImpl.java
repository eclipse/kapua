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

import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinition;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionCreator;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerProperty;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerType;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link TriggerDefinitionCreator} implementation.
 *
 * @since 1.1.0
 */
public class TriggerDefinitionCreatorImpl extends AbstractKapuaNamedEntityCreator<TriggerDefinition> implements TriggerDefinitionCreator {

    private static final long serialVersionUID = 4602067255120049746L;

    private TriggerType triggerType;
    private String processorName;
    private List<TriggerProperty> triggerProperties;

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId} for the Trigger.
     * @since 1.1.0
     */
    public TriggerDefinitionCreatorImpl(KapuaId scopeId) {
        super(scopeId);
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
