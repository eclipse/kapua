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

import org.eclipse.kapua.model.query.KapuaListResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link JobDeviceManagementOperationListResult} definition.
 *
 * @since 1.1.0
 */
@XmlRootElement(name = "jobDeviceManagementOperationListResult")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = JobDeviceManagementOperationXmlRegistry.class, factoryMethod = "newJobDeviceManagementOperationListResult")
public interface JobDeviceManagementOperationListResult extends KapuaListResult<JobDeviceManagementOperation> {

}
