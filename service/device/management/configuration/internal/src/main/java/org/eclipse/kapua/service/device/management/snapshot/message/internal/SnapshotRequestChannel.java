/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.snapshot.message.internal;

import org.eclipse.kapua.service.device.management.commons.message.request.KapuaRequestChannelImpl;

/**
 * Device snapshot request channel.
 *
 * @since 1.0
 */
public class SnapshotRequestChannel extends KapuaRequestChannelImpl {

    private String snapshotId;

    /**
     * Get the snapshot identifier
     *
     * @return
     */
    public String getSnapshotId() {
        return snapshotId;
    }

    /**
     * Set the snapshot identifier
     *
     * @param snapshotId
     */
    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }
}
