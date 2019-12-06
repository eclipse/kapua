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

import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;

/**
 * {@link Trigger} {@link org.eclipse.kapua.model.KapuaEntity} definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "schedule")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = TriggerXmlRegistry.class, factoryMethod = "newTrigger")
public interface Trigger extends KapuaNamedEntity {

    String TYPE = "trigger";

    @Override
    default String getType() {
        return TYPE;
    }

    /**
     * Gets the start {@link Date} from which this {@link Trigger} is valid.
     *
     * @return The start {@link Date} from which this {@link Trigger} is valid.
     * @since 1.0.0
     */
    Date getStartsOn();

    /**
     * Sets the start {@link Date} from which this {@link Trigger} is valid.
     *
     * @param starstOn The start {@link Date} from which this {@link Trigger} is valid.
     * @since 1.0.0
     */
    void setStartsOn(Date starstOn);

    /**
     * Gets the end {@link Date} until which this {@link Trigger} is valid.
     * <p>
     * {@code null} means that never expires.
     *
     * @return The start {@link Date} from which this {@link Trigger} is valid.
     * @since 1.0.0
     */
    Date getEndsOn();

    /**
     * Gets the end {@link Date} until which this {@link Trigger} is valid.
     * <p>
     * {@code null} means that never expires.
     *
     * @param endsOn The end {@link Date} until which this {@link Trigger} is valid.
     * @since 1.0.0
     */
    void setEndsOn(Date endsOn);

    /**
     * Gets the CRON scheduling.
     * <p>
     * This field is {@code deprecated}. Please make use of {@link org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinition} and {@link TriggerProperty}es.
     *
     * @return The CRON scheduling.
     * @since 1.0.0
     * @deprecated since 1.1.0
     */
    @Deprecated
    String getCronScheduling();

    /**
     * Sets the CRON scheduling.
     * <p>
     * This field is {@code deprecated}. Please make use of {@link org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinition} and {@link TriggerProperty}es.
     *
     * @param cronScheduling The CRON scheduling.
     * @since 1.0.0
     * @deprecated since 1.1.0
     */
    @Deprecated
    void setCronScheduling(String cronScheduling);

    /**
     * Gets the retry interval.
     * <p>
     * This field is {@code deprecated}. Please make use of {@link org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinition} and {@link TriggerProperty}es.
     *
     * @return The retry interval.
     * @since 1.0.0
     * @deprecated since 1.1.0
     */
    @Deprecated
    Long getRetryInterval();

    /**
     * Sets the retry interval.
     * <p>
     * This field is {@code deprecated}. Please make use of {@link org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinition} and {@link TriggerProperty}es.
     *
     * @param retryInterval The retry interval.
     * @since 1.0.0
     * @deprecated since 1.1.0
     */
    @Deprecated
    void setRetryInterval(Long retryInterval);

    /**
     * Gets the {@link org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinition} {@link KapuaId} which this {@link Trigger} refers to.
     *
     * @return The {@link org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinition} {@link KapuaId} which this {@link Trigger} refers to.
     * @since 1.1.0
     */
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getTriggerDefinitionId();

    /**
     * Sets the {@link org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinition} {@link KapuaId} which this {@link Trigger} refers to.
     *
     * @param triggerDefinitionId The {@link org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinition} {@link KapuaId} which this {@link Trigger} refers to.
     * @since 1.1.0
     */
    void setTriggerDefinitionId(KapuaId triggerDefinitionId);

    /**
     * Gets the {@link List} of {@link TriggerProperty}es associated with this {@link Trigger}
     *
     * @return The {@link List} of {@link TriggerProperty}es associated with this {@link Trigger}
     * @since 1.0.0
     */
    List<TriggerProperty> getTriggerProperties();

    /**
     * Sets the {@link List} of {@link TriggerProperty}es associated with this {@link Trigger}
     *
     * @param triggerProperties The {@link List} of {@link TriggerProperty}es associated with this {@link Trigger}
     * @since 1.0.0
     */
    void setTriggerProperties(List<TriggerProperty> triggerProperties);
}
