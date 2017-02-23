/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message;

import java.util.Date;

import org.eclipse.kapua.message.Message;

/**
 * Device message definition.
 * 
 * @param <C> channel type
 * @param <P> payload type
 * 
 * @since 1.0
 * 
 */
public interface DeviceMessage<C extends DeviceChannel, P extends DevicePayload> extends Message<C,P>
{

    /**
     * Get the channel associated to the message
     * 
     * @return
     */
    public C getChannel();

    /**
     * Get the payload associated to the message
     * 
     * @return
     */
    public P getPayload();

    /**
     * Get the message timestamp
     * 
     * @return
     */
    public Date getTimestamp();
}
