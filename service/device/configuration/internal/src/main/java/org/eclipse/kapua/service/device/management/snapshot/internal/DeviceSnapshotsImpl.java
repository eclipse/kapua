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
