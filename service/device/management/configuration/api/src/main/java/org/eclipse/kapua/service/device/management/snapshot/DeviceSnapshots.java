/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.snapshot;

import org.eclipse.kapua.KapuaSerializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Device snapshots entity definition.<br>
 * This entity manages a list of {@link DeviceSnapshot}
 *
 * @since 1.0
 */
@XmlRootElement(name = "snapshots")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "snapshots" }, factoryClass = DeviceSnapshotXmlRegistry.class, factoryMethod = "newDeviceSnapshots")
public interface DeviceSnapshots extends KapuaSerializable {

    /**
     * Get the device snapshot list
     *
     * @return
     */
    @XmlElement(name = "snapshotId")
    List<DeviceSnapshot> getSnapshots();
}
