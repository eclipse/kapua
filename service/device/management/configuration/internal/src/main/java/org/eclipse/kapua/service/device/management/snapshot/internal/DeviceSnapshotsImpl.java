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
package org.eclipse.kapua.service.device.management.snapshot.internal;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshot;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshots;

/**
 * Device snapshots entity implementation.
 *
 * @since 1.0
 *
 */
@XmlRootElement(name = "snapshotIds")
public class DeviceSnapshotsImpl implements DeviceSnapshots {

    private static final long serialVersionUID = -7831418953347834946L;

    @XmlElement(name = "snapshotId")
    private List<DeviceSnapshot> snapshotIds;

    @Override
    public List<DeviceSnapshot> getSnapshots() {
        if (snapshotIds == null) {
            snapshotIds = new ArrayList<>();
        }

        return snapshotIds;
    }
}
