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
package org.eclipse.kapua.service.scheduler.trigger;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.model.KapuaNamedEntity;

/**
 * User schedule entity.
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "schedule")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = TriggerXmlRegistry.class, factoryMethod = "newTrigger")
public interface Trigger extends KapuaNamedEntity {

    public static final String TYPE = "schedule";

    public default String getType() {
        return TYPE;
    }

    public Date getStartsOn();

    public void setStartsOn(Date starstOn);

    public Date getEndsOn();

    public void setEndsOn(Date endsOn);

    public String getCronScheduling();

    public void setCronScheduling(String cronScheduling);

    public Long getRetryInterval();

    public void setRetryInterval(Long retryInterval);

    public <P extends TriggerProperty> List<P> getTriggerProperties();

    public void setTriggerProperties(List<TriggerProperty> triggerProperties);
}