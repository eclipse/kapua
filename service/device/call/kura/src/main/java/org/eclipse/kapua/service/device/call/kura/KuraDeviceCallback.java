/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura;

import org.eclipse.kapua.service.device.call.DeviceCallback;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMessage;

/**
 * {@link DeviceCallback} {@link Kura} implementation.
 *
 * @since 1.0.0
 */
public class KuraDeviceCallback implements DeviceCallback<KuraResponseMessage> {

    private KuraDeviceResponseContainer responseContainer;

    /**
     * Constructor.
     *
     * @param responseContainer The {@link KuraDeviceResponseContainer}.
     * @since 1.0.0
     */
    public KuraDeviceCallback(KuraDeviceResponseContainer responseContainer) {
        this.responseContainer = responseContainer;
    }

    @Override
    public void responseReceived(KuraResponseMessage response) {
        responseContainer.add(response);
    }

    @Override
    public void timedOut() {
        notifyAll();
    }
}
