/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.job;

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link JobDeviceManagementOperation} entity.
 *
 * @since 1.1.0
 */
@XmlRootElement(name = "jobDeviceManagementOperation")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = JobDeviceManagementOperationXmlRegistry.class, factoryMethod = "newJobDeviceManagementOperation")
public interface JobDeviceManagementOperation extends KapuaUpdatableEntity {

    String TYPE = "jobDeviceManagementOperation";

    @Override
    default String getType() {
        return TYPE;
    }

    KapuaId getJobId();

    void setJobId(KapuaId jobId);

    KapuaId getDeviceManagementOperationId();

    void setDeviceManagementOperationId(KapuaId deviceManagementOperationId);

}
