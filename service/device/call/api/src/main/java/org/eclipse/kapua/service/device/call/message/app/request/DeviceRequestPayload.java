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
package org.eclipse.kapua.service.device.call.message.app.request;

import org.eclipse.kapua.service.device.call.message.app.DeviceAppPayload;

/**
 * {@link DeviceRequestPayload} definition.
 *
 * @since 1.0.0
 */
public interface DeviceRequestPayload extends DeviceAppPayload {

    /**
     * Gets the request identifier.
     *
     * @return The request identifier.
     * @since 1.0.0
     */
    String getRequestId();

    /**
     * Sets the request identifier.
     *
     * @param requestId The request identifier.
     * @since 1.0.0
     */
    void setRequestId(String requestId);

    /**
     * Gets the requester client identifier.
     * <p>
     * May be useful to reply only to the requester.
     *
     * @return The requester client identifier.
     * @since 1.0.0
     */
    String getRequesterClientId();

    /**
     * Sets the requester client identifier.
     * <p>
     * May be useful to reply only to the requester.
     *
     * @param requesterClientId The requester client identifier.
     * @since 1.0.0
     */
    void setRequesterClientId(String requesterClientId);
}
