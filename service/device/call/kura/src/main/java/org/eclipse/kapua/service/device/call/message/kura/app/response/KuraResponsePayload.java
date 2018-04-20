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
package org.eclipse.kapua.service.device.call.message.kura.app.response;

import org.eclipse.kapua.service.device.call.message.app.response.DeviceResponsePayload;
import org.eclipse.kapua.service.device.call.message.kura.app.KuraAppPayload;

/**
 * {@link DeviceResponsePayload} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 */
public class KuraResponsePayload extends KuraAppPayload implements DeviceResponsePayload {

    @Override
    public KuraResponseCode getResponseCode() {
        return KuraResponseCode.valueOf((String) getMetrics().get("response.code"));
    }

    @Override
    public String getExceptionMessage() {
        return (String) getMetrics().get("response.exception.message");
    }

    @Override
    public String getExceptionStack() {
        return (String) getMetrics().get("response.exception.stack");
    }

    @Override
    public byte[] getResponseBody() {
        return getBody();
    }
}
