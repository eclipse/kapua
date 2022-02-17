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

import org.eclipse.kapua.transport.mqtt.MqttClient;

/**
 * {@link Exception} to {@code throw} when the {@link MqttClient} cannot disconnect properly.
 *
 * @since 1.2.0
 */
public class MqttClientDisconnectException extends MqttClientException {

    public MqttClientDisconnectException(Throwable cause, String clientId) {
        super(MqttClientErrorCodes.DISCONNECT_ERROR, cause, clientId);
    }
}
