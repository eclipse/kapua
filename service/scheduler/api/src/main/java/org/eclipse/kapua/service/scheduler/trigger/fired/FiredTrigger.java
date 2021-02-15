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

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * {@link FiredTrigger} {@link org.eclipse.kapua.model.KapuaEntity} definition.
 *
 * @since 1.5.0
 */
@XmlRootElement(name = "firedTrigger")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = FiredTriggerXmlRegistry.class, factoryMethod = "newEntity")
public interface FiredTrigger extends KapuaEntity {

    String TYPE = "firedTrigger";

    @Override
    default String getType() {
        return TYPE;
    }

    @XmlElement(name = "triggerId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getTriggerId();

    void setTriggerId(KapuaId triggerId);

    @XmlElement(name = "firedOn")
    Date getFiredOn();

    void setFiredOn(Date firedOn);

    @XmlElement(name = "status")
    FiredTriggerStatus getStatus();

    void setStatus(FiredTriggerStatus status);

    @XmlElement(name = "message")
    String getMessage();

    void setMessage(String message);
}
