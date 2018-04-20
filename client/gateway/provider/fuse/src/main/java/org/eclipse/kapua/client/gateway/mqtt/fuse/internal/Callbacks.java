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
package org.eclipse.kapua.client.gateway.mqtt.fuse.internal;

import java.util.concurrent.CompletableFuture;

import org.fusesource.mqtt.client.Callback;

public final class Callbacks {

    private Callbacks() {
    }

    public static <T> Callback<T> asCallback(final CompletableFuture<T> future) {
        return new Callback<T>() {

            @Override
            public void onSuccess(T value) {
                future.complete(value);
            }

            @Override
            public void onFailure(Throwable value) {
                future.completeExceptionally(value);
            }
        };
    }
}
