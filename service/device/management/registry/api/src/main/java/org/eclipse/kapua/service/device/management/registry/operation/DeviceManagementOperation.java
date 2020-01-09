/*******************************************************************************
 * Copyright (c) 2018, 2019 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.model.xml.DateXmlAdapter;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.management.message.notification.OperationStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;

/**
 * @since 1.0.0
 */
@XmlRootElement(name = "deviceManagementOperation")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DeviceManagementOperationXmlRegistry.class, factoryMethod = "newDeviceManagementOperation")
public interface DeviceManagementOperation extends KapuaUpdatableEntity {

    static final String TYPE = "deviceManagementOperation";

    @Override
    default String getType() {
        return TYPE;
    }

    /**
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "startedOn")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getStartedOn();

    /**
     * @param startedOn
     * @since 1.0.0
     */
    void setStartedOn(Date startedOn);

    /**
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "endedOn")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getEndedOn();

    /**
     * @param endedOn
     * @since 1.0.0
     */
    void setEndedOn(Date endedOn);

    /**
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "deviceId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getDeviceId();

    /**
     * @param deviceId
     * @since 1.0.0
     */
    void setDeviceId(KapuaId deviceId);

    /**
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "operationId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getOperationId();

    /**
     * @param operationId
     * @since 1.0.0
     */
    void setOperationId(KapuaId operationId);

    /**
     * @return
     */
    @XmlElement(name = "appId")
    String getAppId();

    /**
     * @param appId
     * @since 1.0.0
     */
    void setAppId(String appId);

    /**
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "action")
    KapuaMethod getAction();

    /**
     * @param action
     * @since 1.0.0
     */
    void setAction(KapuaMethod action);

    /**
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "resource")
    String getResource();

    /**
     * @param resource
     * @since 1.0.0
     */
    void setResource(String resource);

    /**
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "status")
    OperationStatus getStatus();

    /**
     * @param status
     * @since 1.0.0
     */
    void setStatus(OperationStatus status);

    /**
     * @param <P>
     * @return
     * @since 1.0.0
     */
    @XmlElementWrapper(name = "operationProperties")
    @XmlElement(name = "operationProperty")
    <P extends DeviceManagementOperationProperty> List<P> getInputProperties();

    /**
     * @param inputProperties
     * @since 1.0.0
     */
    void setInputProperties(List<DeviceManagementOperationProperty> inputProperties);

    /**
     * @return
     * @since 1.2.0
     */
    String getLog();

    /**
     * @param log
     * @since 1.2.0
     */
    void setLog(String log);

}
