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

    Date getStartsOn();

    void setStartsOn(Date starstOn);

    Date getEndsOn();

    void setEndsOn(Date endsOn);

    String getCronScheduling();

    void setCronScheduling(String cronScheduling);

    Long getRetryInterval();

    void setRetryInterval(Long retryInterval);

    <P extends TriggerProperty> List<P> getTriggerProperties();

    void setTriggerProperties(List<TriggerProperty> triggerProperties);
}
