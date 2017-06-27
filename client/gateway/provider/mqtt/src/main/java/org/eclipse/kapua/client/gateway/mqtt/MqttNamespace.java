/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
