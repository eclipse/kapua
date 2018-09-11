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
package org.eclipse.kapua.service.device.management.registry.operation.notification;

import io.swagger.annotations.ApiModelProperty;
import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.model.xml.DateXmlAdapter;
import org.eclipse.kapua.service.device.management.message.notification.OperationStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

@XmlRootElement(name = "deviceManagementOperationNotificationCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = ManagementOperationNotificationXmlRegistry.class, factoryMethod = "newManagementOperationNotificationCreator")
public interface ManagementOperationNotificationCreator extends KapuaEntityCreator<ManagementOperationNotification> {

    @XmlElement(name = "operationId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    KapuaId getOperationId();

    void setOperationId(KapuaId operationId);

    @XmlElement(name = "sentOn")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getSentOn();

    void setSentOn(Date sentOn);

    @XmlElement(name = "status")
    OperationStatus getStatus();

    void setStatus(OperationStatus status);

    @XmlElement(name = "progress")
    Integer getProgress();

    void setProgress(Integer progress);
}
