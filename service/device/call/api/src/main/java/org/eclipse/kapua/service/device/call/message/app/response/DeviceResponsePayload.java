/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.app.response;

import org.eclipse.kapua.service.device.call.message.app.DeviceAppPayload;

/**
 * Device response {@link DeviceAppPayload} definition.
 */
public interface DeviceResponsePayload extends DeviceAppPayload {

    /**
     * Get the command response code
     *
     * @return
     */
    <C extends DeviceResponseCode> C getResponseCode();

    /**
     * Get the command exception message
     *
     * @return
     */
    String getExceptionMessage();

    /**
     * Get the command exception stack
     *
     * @return
     */
    String getExceptionStack();

    /**
     * Get the command response body
     *
     * @return
     */
    byte[] getResponseBody();

}
