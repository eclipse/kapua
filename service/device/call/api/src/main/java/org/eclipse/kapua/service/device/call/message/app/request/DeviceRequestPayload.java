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
package org.eclipse.kapua.service.device.call.message.app.request;

import org.eclipse.kapua.service.device.call.message.DevicePayload;

/**
 * Device request payload definition.
 * 
 * @since 1.0
 *
 */
public interface DeviceRequestPayload extends DevicePayload
{

    /**
     * Get the request identifier
     * 
     * @return
     */
    public String getRequestId();

    /**
     * Set the request identifier
     * 
     * @param requestId
     */
    public void setRequestId(String requestId);

    /**
     * Get the requester client identifier.<br>
     * May be useful to reply only to the requester
     * 
     * @return
     */
    public String getRequesterClientId();

    /**
     * Set the requester client identifier.<br>
     * May be useful to reply only to the requester
     * 
     * @param requesterId
     */
    public void setRequesterClientId(String requesterId);
}
