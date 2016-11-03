/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.snapshot;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.KapuaSerializable;

/**
 * Device snapshots entity definition.<br>
 * This entity manages a list of {@link DeviceSnapshot}
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "snapshots")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "snapshots" },
        factoryClass = DeviceSnapshotXmlRegistry.class, 
        factoryMethod = "newDeviceSnapshots")
public interface DeviceSnapshots extends KapuaSerializable
{

    /**
     * Get the device snapshot list
     * 
     * @return
     */
    @XmlElement(name="snapshotId")
    public List<DeviceSnapshot> getSnapshots();
}
