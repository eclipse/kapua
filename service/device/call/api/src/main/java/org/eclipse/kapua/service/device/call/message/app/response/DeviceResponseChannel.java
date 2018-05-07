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
package org.eclipse.kapua.service.device.call.message.app.response;

import org.eclipse.kapua.service.device.call.message.app.DeviceAppChannel;

/**
 * Device response channel definition.
 *
 * @since 1.0
 */
public interface DeviceResponseChannel extends DeviceAppChannel {

    /**
     * Get the reply part
     *
     * @return
     */
    String getReplyPart();

    /**
     * Set the reply part
     *
     * @param replyPart
     */
    void setReplyPart(String replyPart);

    /**
     * Get the request identifier
     *
     * @return
     */
    String getRequestId();

    /**
     * Set the request identifier
     *
     * @param requestId
     */
    void setRequestId(String requestId);
}
