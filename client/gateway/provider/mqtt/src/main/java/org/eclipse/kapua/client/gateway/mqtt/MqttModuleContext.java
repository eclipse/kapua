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

import java.nio.ByteBuffer;
import java.util.concurrent.CompletionStage;

/**
 * Module context when working with MQTT backed clients
 */
public interface MqttModuleContext {

    public CompletionStage<?> publishMqtt(String topic, ByteBuffer payload);

    public String getMqttClientId();

}