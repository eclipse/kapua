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

import org.eclipse.kapua.service.device.call.DeviceMethod;
import org.eclipse.kapua.service.device.call.message.app.DeviceAppChannel;

/**
 * Device request channel definition.
 * 
 * @since 1.0
 *
 */
public interface DeviceRequestChannel extends DeviceAppChannel {

    /**
     * Get the request method
     * 
     * @return
     */
    public DeviceMethod getMethod();

    /**
     * Set the request method
     * 
     * @param method
     */
    public void setMethod(DeviceMethod method);

    /**
     * Get the request resources
     * 
     * @return
     */
    public String[] getResources();

    /**
     * Set the request resources
     * 
     * @param resources
     */
    public void setResources(String[] resources);

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
     * @param requesterClientId
     */
    public void setRequesterClientId(String requesterClientId);

}
