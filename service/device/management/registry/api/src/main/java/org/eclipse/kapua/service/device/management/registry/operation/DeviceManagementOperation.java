/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.registry.operation;

import io.swagger.annotations.ApiModelProperty;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.model.xml.DateXmlAdapter;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;

@XmlRootElement(name = "deviceManagementOperation")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DeviceManagementOperationXmlRegistry.class, factoryMethod = "newDeviceManagementOperation")
public interface DeviceManagementOperation extends KapuaUpdatableEntity {

    static final String TYPE = "deviceManagementOperation";

    @Override
    default String getType() {
        return TYPE;
    }

    @XmlElement(name = "startedOn")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getStartedOn();

    @XmlElement(name = "endedOn")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getEndedOn();

    void setEndedOn(Date endedOn);

    @XmlElement(name = "deviceId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    KapuaId getDeviceId();

    void setDeviceId(KapuaId deviceId);

    @XmlElement(name = "operationId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    KapuaId getOperationId();

    void setOperationId(KapuaId operationId);

    @XmlElement(name = "appId")
    String getAppId();

    void setAppId(String appId);

    @XmlElement(name = "action")
    KapuaMethod getAction();

    void setAction(KapuaMethod action);

    @XmlElement(name = "resource")
    String getResource();

    void setResource(String resource);

    @XmlElement(name = "status")
    OperationStatus getStatus();

    void setStatus(OperationStatus status);

    @XmlElementWrapper(name = "operationProperties")
    @XmlElement(name = "operationProperty")
    <P extends DeviceManagementOperationProperty> List<P> getInputProperties();

    void setInputProperties(List<DeviceManagementOperationProperty> inputProperties);

}
