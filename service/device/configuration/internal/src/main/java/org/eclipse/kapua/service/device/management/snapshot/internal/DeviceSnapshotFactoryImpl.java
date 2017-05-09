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
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.snapshot.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshot;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshotFactory;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshots;

/**
 * Device snapshot entity service factory implementation.
 *
 * @since 1.0
 * 
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
