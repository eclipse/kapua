/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura.app.request;

import org.eclipse.kapua.service.device.call.message.app.request.DeviceRequestPayload;
import org.eclipse.kapua.service.device.call.message.kura.app.KuraAppPayload;

/**
 * {@link DeviceRequestPayload} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation
 */
public class KuraRequestPayload extends KuraAppPayload implements DeviceRequestPayload {

    @Override
    public void setRequestId(String requestId) {
        getMetrics().put(KuraRequestMetrics.REQUEST_ID.getValue(), requestId);
    }

    @Override
    public String getRequestId() {
        return (String) getMetrics().get(KuraRequestMetrics.REQUEST_ID.getValue());
    }

    @Override
    public void setRequesterClientId(String requesterClientId) {
        getMetrics().put(KuraRequestMetrics.REQUESTER_CLIENT_ID.getValue(), requesterClientId);
    }

    @Override
    public String getRequesterClientId() {
        return (String) getMetrics().get(KuraRequestMetrics.REQUESTER_CLIENT_ID.getValue());
    }
}
