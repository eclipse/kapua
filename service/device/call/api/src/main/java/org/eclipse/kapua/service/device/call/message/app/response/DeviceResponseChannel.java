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
package org.eclipse.kapua.service.device.call.message.app.response;

import org.eclipse.kapua.service.device.call.message.app.DeviceAppChannel;

/**
 * {@link DeviceResponseChannel} definition.
 *
 * @since 1.0.0
 */
public interface DeviceResponseChannel extends DeviceAppChannel {

    /**
     * Gets the reply part.
     *
     * @return The reply part.
     * @since 1.0.0
     */
    String getReplyPart();

    /**
     * Sets the reply part.
     *
     * @param replyPart The reply part.
     * @since 1.0.0
     */
    void setReplyPart(String replyPart);

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
}
