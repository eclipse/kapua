/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.devicegroup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * *DeviceGroup entity
 * 
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "devId",
        "groupId" }, factoryClass = DeviceGroupXmlRegistry.class, factoryMethod = "newDeviceGroup")
public interface DeviceGroup extends KapuaUpdatableEntity {

    public KapuaId getDevId();

    public void setDevId(KapuaId devId);

    public KapuaId getGroupId();

    public void setGroupId(KapuaId groupId);

}
