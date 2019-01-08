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
package org.eclipse.kapua.service.device.management.registry.operation.notification;

import org.eclipse.kapua.model.query.KapuaListResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link ManagementOperationNotificationListResult} definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "managementOperationNotificationListResult")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = ManagementOperationNotificationXmlRegistry.class, factoryMethod = "newManagementOperationNotificationListResult")
public interface ManagementOperationNotificationListResult extends KapuaListResult<ManagementOperationNotification> {

}
