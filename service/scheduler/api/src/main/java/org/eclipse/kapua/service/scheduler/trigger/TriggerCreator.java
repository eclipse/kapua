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
package org.eclipse.kapua.service.scheduler.trigger;

import org.eclipse.kapua.model.KapuaNamedEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;
import java.util.List;

/**
 * {@link TriggerCreator} {@link org.eclipse.kapua.model.KapuaEntityCreator} definition
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "triggerCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = TriggerXmlRegistry.class, factoryMethod = "newTriggerCreator")
public interface TriggerCreator extends KapuaNamedEntityCreator<Trigger> {

    /**
     * @since 1.0.0
     */
    Date getStartsOn();

    /**
     * @since 1.0.0
     */
    void setStartsOn(Date starstOn);

    /**
     * @since 1.0.0
     */
    Date getEndsOn();

    /**
     * @since 1.0.0
     */
    void setEndsOn(Date endsOn);

    /**
     * @since 1.0.0
     * @deprecated since 1.1.0
     */
    @Deprecated
    String getCronScheduling();

    /**
     * @since 1.0.0
     * @deprecated since 1.1.0
     */
    @Deprecated
    void setCronScheduling(String cronScheduling);

    /**
     * @since 1.0.0
     * @deprecated since 1.1.0
     */
    @Deprecated
    Long getRetryInterval();

    /**
     * @since 1.0.0
     * @deprecated since 1.1.0
     */
    @Deprecated
    void setRetryInterval(Long retryInterval);

    /**
     * @since 1.1.0
     */
    KapuaId getTriggerDefinitionId();

    /**
     * @since 1.1.0
     */
    void setTriggerDefinitionId(KapuaId triggerDefinitionId);

    /**
     * @since 1.0.0
     */
    List<TriggerProperty> getTriggerProperties();

    /**
     * @since 1.0.0
     */
    void setTriggerProperties(List<TriggerProperty> triggerProperties);
}
