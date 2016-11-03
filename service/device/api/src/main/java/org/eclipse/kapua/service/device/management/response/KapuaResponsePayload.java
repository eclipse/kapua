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
package org.eclipse.kapua.service.device.management.response;

import org.eclipse.kapua.message.KapuaPayload;

/**
 * Kapua response message payload definition.<br>
 * This object defines the payload for a Kapua response message.<br>
 * The response message is used to perform interactive operations with the device (e.g. to send command to the device, to ask configurations...)
 * 
 * @since 1.0
 *
 */
public interface KapuaResponsePayload extends KapuaPayload
{

    /**
     * Get the exception message (if present)
     * 
     * @return
     */
    public String getExceptionMessage();

    /**
     * Set the exception message (if present)
     * 
     * @param setExecptionMessage
     */
    public void setExceptionMessage(String setExecptionMessage);

    /**
     * Get the exception stack trace (if present)
     * 
     * @return
     */
    public String getExceptionStack();

    /**
     * Set the exception stack trace (if present)
     * 
     * @param setExecptionStack
     */
    public void setExceptionStack(String setExecptionStack);
}
