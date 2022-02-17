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
package org.eclipse.kapua.service.device.management.snapshot.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshot;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshotFactory;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshots;

/**
 * {@link DeviceSnapshotFactory} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class DeviceSnapshotFactoryImpl implements DeviceSnapshotFactory {

    @Override
    public DeviceSnapshot newDeviceSnapshot() {
        return new DeviceSnapshotImpl();
    }

    @Override
    public DeviceSnapshots newDeviceSnapshots() {
        return new DeviceSnapshotsImpl();
    }

}
