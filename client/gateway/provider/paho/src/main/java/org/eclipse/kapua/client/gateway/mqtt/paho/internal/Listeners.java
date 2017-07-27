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
package org.eclipse.kapua.client.gateway.mqtt.paho.internal;

import java.util.concurrent.CompletableFuture;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

public final class Listeners {

    private Listeners() {
    }

    public static IMqttActionListener toListener(final CompletableFuture<?> token) {
        return new IMqttActionListener() {

            @Override
            public void onSuccess(final IMqttToken asyncActionToken) {
                token.complete(null);
            }

            @Override
            public void onFailure(final IMqttToken asyncActionToken, final Throwable exception) {
                token.completeExceptionally(exception);
            }
        };
    }
}
