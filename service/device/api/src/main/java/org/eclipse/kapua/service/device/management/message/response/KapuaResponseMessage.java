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
package org.eclipse.kapua.service.device.management.message.response;

import org.eclipse.kapua.message.KapuaMessage;

/**
 * Kapua response message definition.<br>
 * This object defines the for a Kapua response message.<br>
 * The response message is used to perform interactive operations with the device (e.g. to send command to the device, to ask configurations...)
 *
 * @since 1.0
 */
public interface KapuaResponseMessage<C extends KapuaResponseChannel, P extends KapuaResponsePayload> extends KapuaMessage<C, P> {

    /**
     * Get the response code
     *
     * @return
     */
    public KapuaResponseCode getResponseCode();

    /**
     * Set the response code
     *
     * @param responseCode
     */
    public void setResponseCode(KapuaResponseCode responseCode);
}
