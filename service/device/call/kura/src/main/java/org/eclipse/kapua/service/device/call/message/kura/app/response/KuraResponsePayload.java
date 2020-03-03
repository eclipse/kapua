/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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
 *
 * @since 1.0.0
 */
public class KuraResponsePayload extends KuraAppPayload implements DeviceResponsePayload {

    @Override
    public KuraResponseCode getResponseCode() {
        return KuraResponseCode.fromResponseCode((Integer) getMetrics().get(KuraResponseMetrics.EXIT_CODE.getName()));
    }

    @Override
    public String getExceptionMessage() {
        return (String) getMetrics().get(KuraResponseMetrics.EXCEPTION_MESSAGE.getName());
    }

    @Override
    public String getExceptionStack() {
        return (String) getMetrics().get(KuraResponseMetrics.EXCEPTION_STACK.getName());
    }

    @Override
    public byte[] getResponseBody() {
        return getBody();
    }
}
