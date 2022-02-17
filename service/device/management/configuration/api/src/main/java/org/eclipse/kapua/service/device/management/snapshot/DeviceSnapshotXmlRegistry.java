/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link DeviceSnapshot} xml factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class DeviceSnapshotXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DeviceSnapshotFactory DEVICE_SNAPSHOT_FACTORY = LOCATOR.getFactory(DeviceSnapshotFactory.class);

    /**
     * Creates a new device snapshots list
     *
     * @return
     */
    public DeviceSnapshots newDeviceSnapshots() {
        return DEVICE_SNAPSHOT_FACTORY.newDeviceSnapshots();
    }

    /**
     * Creates a new device snapshot
     *
     * @return
     */
    public DeviceSnapshot newDeviceSnapshot() {
        return DEVICE_SNAPSHOT_FACTORY.newDeviceSnapshot();
    }
}
