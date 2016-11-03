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

import org.eclipse.kapua.message.internal.KapuaMessageImpl;
import org.eclipse.kapua.service.device.management.response.KapuaResponseCode;
import org.eclipse.kapua.service.device.management.response.KapuaResponseMessage;

/**
 * Device snapshot response message.
 * 
 * @since 1.0
 * 
 */
public class SnapshotResponseMessage extends KapuaMessageImpl<SnapshotResponseChannel, SnapshotResponsePayload>implements KapuaResponseMessage<SnapshotResponseChannel, SnapshotResponsePayload>
{
    private KapuaResponseCode responseCode;

    @Override
    public KapuaResponseCode getResponseCode()
    {
        return responseCode;
    }

    @Override
    public void setResponseCode(KapuaResponseCode responseCode)
    {
        this.responseCode = responseCode;
    }
}
