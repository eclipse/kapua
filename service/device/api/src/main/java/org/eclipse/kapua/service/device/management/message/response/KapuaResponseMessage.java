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
    KapuaResponseCode getResponseCode();

    /**
     * Set the response code
     *
     * @param responseCode
     */
    void setResponseCode(KapuaResponseCode responseCode);
}
