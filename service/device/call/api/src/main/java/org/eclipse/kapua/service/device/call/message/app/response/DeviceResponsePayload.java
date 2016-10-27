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
package org.eclipse.kapua.service.device.call.message.app.response;

import org.eclipse.kapua.service.device.call.message.DevicePayload;

/**
 * Device response payload definition.
 * 
 * @since 1.0
 *
 */
public interface DeviceResponsePayload extends DevicePayload
{

    /**
     * Get the command response code
     * 
     * @return
     */
    public <C extends DeviceResponseCode> C getResponseCode();

    /**
     * Get the command exception message
     * 
     * @return
     */
    public String getExceptionMessage();

    /**
     * Get the command exception stack
     * 
     * @return
     */
    public String getExceptionStack();

    /**
     * Get the command response body
     * 
     * @return
     */
    public byte[] getResponseBody();

}
