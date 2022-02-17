/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.gateway.mqtt;

import org.eclipse.kapua.client.gateway.Topic;

/**
 * A namespace implementation for MQTT
 */
public interface MqttNamespace {

    /**
     * Render an MQTT topic for the provided data topic
     *
     * @param clientId
     *            The MQTT client ID
     * @param applicationId
     *            The application ID
     * @param topic
     *            The data topid
     * @return The topic or {@code null} if no topic could be rendered for the provided input parameters
     */
    public String dataTopic(String clientId, String applicationId, Topic topic);
}
