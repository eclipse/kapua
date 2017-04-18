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
package org.eclipse.kapua.service.device.management.channel.message.internal;

import org.eclipse.kapua.message.internal.KapuaMessageImpl;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetRequestPayload;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetResponseMessage;
import org.eclipse.kapua.service.device.management.request.KapuaRequestMessage;

/**
 * Device bundle information request message.
 * 
 * @since 1.0
 * 
 */
public class ChannelRequestMessage extends KapuaMessageImpl<ChannelRequestChannel, ChannelRequestPayload>
                                         implements KapuaRequestMessage<ChannelRequestChannel, ChannelRequestPayload>
{
    @SuppressWarnings("unchecked")
    @Override
    public Class<ChannelRequestMessage> getRequestClass()
    {
        return ChannelRequestMessage.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<ChannelResponseMessage> getResponseClass()
    {
        return ChannelResponseMessage.class;
    }

}
