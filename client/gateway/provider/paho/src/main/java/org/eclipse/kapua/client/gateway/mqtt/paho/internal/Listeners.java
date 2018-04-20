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
import java.util.function.Consumer;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

public final class Listeners {

    private Listeners() {
    }

    public static IMqttActionListener toListener(final Runnable success, final Consumer<Throwable> failure) {
        return new IMqttActionListener() {

            @Override
            public void onSuccess(final IMqttToken asyncActionToken) {
                if (success != null) {
                    success.run();
                }
            }

            @Override
            public void onFailure(final IMqttToken asyncActionToken, final Throwable exception) {
                if (failure != null) {
                    failure.accept(exception);
                }
            }
        };
    }

    public static IMqttActionListener toListener(final CompletableFuture<?> future) {
        return toListener(() -> future.complete(null), future::completeExceptionally);
    }
}
