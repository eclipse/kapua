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
package org.eclipse.kapua.service.device.call.message.kura.lifecycle;

import java.util.Date;

import org.eclipse.kapua.service.device.call.message.kura.KuraMessage;

public class KuraUnmatchedMessage extends KuraMessage<KuraUnmatchedChannel, KuraUnmatchedPayload>
{

    public KuraUnmatchedMessage()
    {
        super();
    }
    
    public KuraUnmatchedMessage(KuraUnmatchedChannel channel,
                                Date timestamp, KuraUnmatchedPayload payload)
    {
    	this.channel = channel;
    	this.timestamp = timestamp;
    	this.payload = payload;
    }

}
