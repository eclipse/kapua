/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
public class KuraSnapshotIds
{
    private List<Long> snapshotIds;

    /**
     * Constructor
     */
    public KuraSnapshotIds()
    {}

    /**
     * Get the snapshot identifiers list
     * 
     * @return
     */
    public List<Long> getSnapshotIds()
    {
        return snapshotIds;
    }

    /**
     * Set the snapshot identifiers list
     * 
     * @param snapshotIds
     */
    public void setSnapshotIds(List<Long> snapshotIds)
    {
        this.snapshotIds = snapshotIds;
    }
}
