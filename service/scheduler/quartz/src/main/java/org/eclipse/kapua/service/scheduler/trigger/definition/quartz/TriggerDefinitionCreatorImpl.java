/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
 * {@link TriggerDefinitionCreator} implementation
 *
 * @since 1.0.0
 */
public class TriggerDefinitionCreatorImpl extends AbstractKapuaNamedEntityCreator<TriggerDefinition> implements TriggerDefinitionCreator {

    private static final long serialVersionUID = 4602067255120049746L;

    private TriggerType triggerType;
    private String readerName;
    private String processorName;
    private String writerName;
    private List<TriggerProperty> triggerProperties;

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
    public void setTriggerProperties(List<TriggerProperty> triggerProperties) {
        this.triggerProperties = triggerProperties;
    }

}
