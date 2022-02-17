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

import java.nio.ByteBuffer;
import java.util.concurrent.CompletionStage;

/**
 * Module context when working with MQTT backed clients
 */
public interface MqttModuleContext {

    public CompletionStage<?> publishMqtt(String topic, ByteBuffer payload);

    public String getMqttClientId();

}
