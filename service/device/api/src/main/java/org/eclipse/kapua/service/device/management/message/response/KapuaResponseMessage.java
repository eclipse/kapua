/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
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
 * Response {@link KapuaMessage} definition.
 * <p>
 * The response message is used to perform interactive operations with the device (e.g. to send command to the device, to ask configurations...)
 *
 * @since 1.0.0
 */
public interface KapuaResponseMessage<C extends KapuaResponseChannel, P extends KapuaResponsePayload>
        extends KapuaMessage<C, P> {

    @Override
    C getChannel();

    @Override
    void setChannel(C semanticChannel);

    @Override
    P getPayload();

    @Override
    void setPayload(P payload);

    /**
     * Gets the {@link KapuaResponseCode}.
     *
     * @return The {@link KapuaResponseCode}.
     * @since 1.0.0
     */
    KapuaResponseCode getResponseCode();

    /**
     * Sets the {@link KapuaResponseCode}.
     *
     * @param responseCode The {@link KapuaResponseCode}.
     * @since 1.0.0
     */
    void setResponseCode(KapuaResponseCode responseCode);
}
