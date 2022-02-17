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

import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshot;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshots;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DeviceSnapshots} implementation.
 *
 * @since 1.0.0
 */
public class DeviceSnapshotsImpl implements DeviceSnapshots {

    private static final long serialVersionUID = -7831418953347834946L;

    private List<DeviceSnapshot> snapshotIds;

    @Override
    public List<DeviceSnapshot> getSnapshots() {
        if (snapshotIds == null) {
            snapshotIds = new ArrayList<>();
        }

        return snapshotIds;
    }
}
