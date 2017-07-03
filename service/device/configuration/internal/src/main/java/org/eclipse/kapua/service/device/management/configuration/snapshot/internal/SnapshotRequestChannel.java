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
package org.eclipse.kapua.service.device.management.configuration.snapshot.internal;

import org.eclipse.kapua.service.device.management.commons.message.request.KapuaRequestChannelImpl;

/**
 * Device snapshot request channel.
 * 
 * @since 1.0
 * 
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
