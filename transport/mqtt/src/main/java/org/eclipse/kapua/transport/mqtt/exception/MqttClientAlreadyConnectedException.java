/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.transport.mqtt.exception;

import org.eclipse.kapua.transport.TransportClientConnectOptions;
import org.eclipse.kapua.transport.mqtt.MqttClient;

/**
 * {@link Exception} to {@code throw} when the {@link MqttClient} is already connected and {@link MqttClient#connectClient(TransportClientConnectOptions)} is invoked.
 *
 * @since 1.2.0
 */
public class MqttClientAlreadyConnectedException extends MqttClientException {

    /**
     * Constructor.
     *
     * @param clientId The clientId of the {@link org.eclipse.kapua.transport.mqtt.MqttClient} that produced this {@link MqttClientCleanException}.
     * @since 1.2.0
     */
    public MqttClientAlreadyConnectedException(String clientId) {
        super(MqttClientErrorCodes.ALREADY_CONNECTED, clientId);
    }

}
