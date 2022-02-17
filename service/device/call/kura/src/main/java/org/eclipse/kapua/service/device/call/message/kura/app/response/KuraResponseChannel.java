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
package org.eclipse.kapua.service.device.call.message.kura.app.response;

import org.eclipse.kapua.service.device.call.message.app.response.DeviceResponseChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.KuraAppChannel;

import java.util.List;

/**
 * {@link DeviceResponseChannel} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 *
 * @since 1.0.0
 */
public class KuraResponseChannel extends KuraAppChannel implements DeviceResponseChannel {

    /**
     * The reply token.
     *
     * @since 1.0.0
     */
    private String replyPart;

    /**
     * The request id.
     *
     * @since 1.0.0
     */
    private String requestId;

    /**
     * Constructor.
     *
     * @param messageClassification The message classification.
     * @param scopeNamespace        The scope namespace.
     * @param clientId              The clientId.
     * @see org.eclipse.kapua.service.device.call.message.DeviceChannel
     * @since 1.0.0
     */
    public KuraResponseChannel(String messageClassification, String scopeNamespace, String clientId) {
        super(messageClassification, scopeNamespace, clientId);
    }

    @Override
    public String getReplyPart() {
        return replyPart;
    }

    @Override
    public void setReplyPart(String replyPart) {
        this.replyPart = replyPart;
    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    @Override
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public List<String> getParts() {
        List<String> parts = super.getParts();
        parts.add(getReplyPart());
        parts.add(getRequestId());
        return parts;
    }
}
