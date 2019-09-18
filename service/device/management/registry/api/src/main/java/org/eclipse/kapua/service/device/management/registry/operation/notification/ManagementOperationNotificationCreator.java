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
package org.eclipse.kapua.service.device.management.registry.operation.notification;

import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.model.xml.DateXmlAdapter;
import org.eclipse.kapua.service.device.management.message.notification.OperationStatus;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * {@link ManagementOperationNotificationCreator} definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "deviceManagementOperationNotificationCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = ManagementOperationNotificationXmlRegistry.class, factoryMethod = "newManagementOperationNotificationCreator")
public interface ManagementOperationNotificationCreator extends KapuaEntityCreator<ManagementOperationNotification> {

    /**
     * Gets the {@link DeviceManagementOperation#getId()}.
     *
     * @return The {@link DeviceManagementOperation#getId()}.
     * @since 1.0.0
     */
    @XmlElement(name = "operationId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getOperationId();

    /**
     * Sets the {@link DeviceManagementOperation#getId()}.
     *
     * @param operationId The {@link DeviceManagementOperation#getId()}.
     * @since 1.0.0
     */
    void setOperationId(KapuaId operationId);

    /**
     * Gets the {@link Date} of when the notification has been sent to the platform.
     *
     * @return The {@link Date} of when the notification has been sent to the platform.
     * @since 1.0.0
     */
    @XmlElement(name = "sentOn")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getSentOn();

    /**
     * Sets the {@link Date} of when the notification has been sent to the platform.
     *
     * @param sentOn The {@link Date} of when the notification has been sent to the platform.
     * @since 1.0.0
     */
    void setSentOn(Date sentOn);

    /**
     * Gets the {@link OperationStatus}
     *
     * @return The {@link OperationStatus}
     * @since 1.0.0
     */
    @XmlElement(name = "status")
    OperationStatus getStatus();

    /**
     * Sets the {@link OperationStatus}
     *
     * @param status The {@link OperationStatus}
     * @since 1.0.0
     */
    void setStatus(OperationStatus status);

    /**
     * Gets the {@link DeviceManagementOperation#getResource()}
     *
     * @return The {@link DeviceManagementOperation#getResource()}
     * @since 1.0.0
     */
    @XmlElement(name = "resource")
    String getResource();

    /**
     * Sets the {@link DeviceManagementOperation#getResource()}
     *
     * @param resource The {@link DeviceManagementOperation#getResource()}
     * @since 1.0.0
     */
    void setResource(String resource);

    /**
     * Gets the progress percentage of the processing.
     *
     * @return The progress percentage of the processing.
     * @since 1.0.0
     */
    @XmlElement(name = "progress")
    Integer getProgress();

    /**
     * Sets the progress percentage of the processing.
     *
     * @param progress The progress percentage of the processing.
     * @since 1.0.0
     */
    void setProgress(Integer progress);

    /**
     * Gets the detailed message related to the {@link OperationStatus}
     *
     * @return The detailed message related to the {@link OperationStatus}
     * @since 1.2.0
     */
    String getMessage();

    /**
     * Sets the detailed message related to the {@link OperationStatus}
     *
     * @param message The detailed message related to the {@link OperationStatus}
     * @since 1.2.0
     */
    void setMessage(String message);
}
