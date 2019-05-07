/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.scheduler.trigger.definition;

import org.eclipse.kapua.model.KapuaNamedEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * {@link TriggerDefinition} definition.
 *
 * @since 1.1.0
 */
@XmlRootElement(name = "triggerDefinition")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = TriggerDefinitionXmlRegistry.class, factoryMethod = "newTriggerDefinition")
public interface TriggerDefinition extends KapuaNamedEntity {

    String TYPE = "triggerDefinition";

    @Override
    default String getType() {
        return TYPE;
    }


    /**
     * Gets the {@link TriggerType}.
     *
     * @return The {@link TriggerType}.
     * @since 1.1.0
     */
    TriggerType getTriggerType();

    void setTriggerType(TriggerType triggerType);

    String getProcessorName();

    void setProcessorName(String processorName);

    List<TriggerProperty> getTriggerProperties();

    void setTriggerProperties(List<TriggerProperty> triggerProperties);

}
