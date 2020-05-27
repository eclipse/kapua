/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.message.response;

import org.eclipse.kapua.message.KapuaPayload;

/**
 * Kapua response message payload definition.<br>
 * This object defines the payload for a Kapua response message.<br>
 * The response message is used to perform interactive operations with the device (e.g. to send command to the device, to ask configurations...)
 *
 * @since 1.0
 */
public interface KapuaResponsePayload extends KapuaPayload {

    /**
     * Get the exception message (if present)
     *
     * @return
     */
    String getExceptionMessage();

    /**
     * Set the exception message (if present)
     *
     * @param setExecptionMessage
     */
    void setExceptionMessage(String setExecptionMessage);

    /**
     * Get the exception stack trace (if present)
     *
     * @return
     */
    String getExceptionStack();

    /**
     * Set the exception stack trace (if present)
     *
     * @param setExecptionStack
     */
    void setExceptionStack(String setExecptionStack);
}
