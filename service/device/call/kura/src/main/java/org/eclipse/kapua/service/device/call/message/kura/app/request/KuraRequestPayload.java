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
package org.eclipse.kapua.service.device.call.message.kura.app.request;

import org.eclipse.kapua.service.device.call.message.app.request.DeviceRequestPayload;
import org.eclipse.kapua.service.device.call.message.kura.app.KuraAppPayload;

/**
 * {@link DeviceRequestPayload} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 *
 * @since 1.0.0
 */
public class KuraRequestPayload extends KuraAppPayload implements DeviceRequestPayload {

    @Override
    public void setRequestId(String requestId) {
        getMetrics().put(KuraRequestMetrics.REQUEST_ID.getName(), requestId);
    }

    @Override
    public String getRequestId() {
        return (String) getMetrics().get(KuraRequestMetrics.REQUEST_ID.getName());
    }

    @Override
    public void setRequesterClientId(String requesterClientId) {
        getMetrics().put(KuraRequestMetrics.REQUESTER_CLIENT_ID.getName(), requesterClientId);
    }

    @Override
    public String getRequesterClientId() {
        return (String) getMetrics().get(KuraRequestMetrics.REQUESTER_CLIENT_ID.getName());
    }
}
