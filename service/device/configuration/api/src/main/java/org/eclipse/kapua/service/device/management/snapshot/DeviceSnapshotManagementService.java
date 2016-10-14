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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.snapshot;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;

public interface DeviceSnapshotManagementService extends KapuaService
{
    public DeviceSnapshots get(KapuaId scopeId, KapuaId deviceid, Long timeout)
        throws KapuaException;

    public void rollback(KapuaId scopeId, KapuaId deviceid, String snapshotId, Long timeout)
        throws KapuaException;
}
