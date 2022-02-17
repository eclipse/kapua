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
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura;

import org.eclipse.kapua.service.device.call.DeviceCallback;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMessage;

import java.util.ArrayList;

/**
 * {@link DeviceCallback} {@link Kura} implementation.
 *
 * @since 1.0.0
 */
public class KuraDeviceResponseContainer extends ArrayList<KuraResponseMessage> implements DeviceCallback<KuraResponseMessage> {

    private static final long serialVersionUID = -6909761350290400843L;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public KuraDeviceResponseContainer() {
        super();
    }

    @Override
    public void responseReceived(KuraResponseMessage response) {
        synchronized (this) {
            add(response);
            notifyAll();
        }
    }

    @Override
    public void timedOut() {
        synchronized (this) {
            notifyAll();
        }
    }
}
