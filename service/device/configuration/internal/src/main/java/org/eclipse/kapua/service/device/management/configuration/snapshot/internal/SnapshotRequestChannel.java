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
package org.eclipse.kapua.service.device.management.configuration.snapshot.internal;

import org.eclipse.kapua.service.device.management.KapuaMethod;
import org.eclipse.kapua.service.device.management.commons.message.response.KapuaAppChannelImpl;
import org.eclipse.kapua.service.device.management.request.KapuaRequestChannel;

public class SnapshotRequestChannel extends KapuaAppChannelImpl implements KapuaRequestChannel
{
    private KapuaMethod method;
    private String      snapshotId;

    @Override
    public KapuaMethod getMethod()
    {
        return method;
    }

    @Override
    public void setMethod(KapuaMethod method)
    {
        this.method = method;
    }

    public String getSnapshotId()
    {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId)
    {
        this.snapshotId = snapshotId;
    }
}
