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
