/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.scheduler.trigger.fired;

import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

/**
 * {@link FiredTriggerCreator} {@link org.eclipse.kapua.model.KapuaEntityCreator} definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "firedTriggerCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = FiredTriggerXmlRegistry.class, factoryMethod = "newCreator")
public interface FiredTriggerCreator extends KapuaEntityCreator<FiredTrigger> {

    KapuaId getTriggerId();

    void setTriggerId(KapuaId triggerId);

    Date getFiredOn();

    void setFiredOn(Date firedOn);

    FiredTriggerStatus getStatus();

    void setStatus(FiredTriggerStatus status);

    String getMessage();

    void setMessage(String message);
}
