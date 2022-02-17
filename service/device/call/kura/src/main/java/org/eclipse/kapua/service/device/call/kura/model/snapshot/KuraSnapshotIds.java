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
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura.model.snapshot;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Utility class to serialize a set of snapshot ids.
 *
 * @since 1.0
 *
 */
@XmlRootElement(name = "snapshot-ids")
public class KuraSnapshotIds {

    private List<Long> snapshotIds;

    /**
     * Constructor
     */
    public KuraSnapshotIds() {
    }

    /**
     * Get the snapshot identifiers list
     *
     * @return
     */
    public List<Long> getSnapshotIds() {
        return snapshotIds;
    }

    /**
     * Set the snapshot identifiers list
     *
     * @param snapshotIds
     */
    public void setSnapshotIds(List<Long> snapshotIds) {
        this.snapshotIds = snapshotIds;
    }
}
