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
        Integer exitCode = (Integer) getMetrics().get(KuraResponseMetrics.EXIT_CODE.getName());
        return exitCode != null ? KuraResponseCode.fromResponseCode(exitCode) : null;
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
