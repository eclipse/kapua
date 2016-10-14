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
package org.eclipse.kapua.service.device.call.message;

import java.util.Date;

import org.eclipse.kapua.message.Message;

public interface DeviceMessage<C extends DeviceChannel, P extends DevicePayload> extends Message
{
    public C getChannel();

    public P getPayload();

    public Date getTimestamp();
}
