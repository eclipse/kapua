/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.transport.mqtt.exception;

import org.eclipse.kapua.transport.TransportClientConnectOptions;
import org.eclipse.kapua.transport.mqtt.MqttClient;

/**
 * {@link Exception} to {@code throw} when the {@link MqttClient} is not connected and any operation is invoked but {@link MqttClient#connectClient(TransportClientConnectOptions)}.
 *
 * @since 1.2.0
 */
public class MqttClientNotConnectedException extends MqttClientException {

    /**
     * Constructor.
     *
     * @param clientId The clientId of the {@link org.eclipse.kapua.transport.mqtt.MqttClient} that produced this {@link MqttClientNotConnectedException}.
     * @since 1.2.0
     */
    public MqttClientNotConnectedException(String clientId) {
        super(MqttClientErrorCodes.NOT_CONNECTED, clientId);
    }
}
