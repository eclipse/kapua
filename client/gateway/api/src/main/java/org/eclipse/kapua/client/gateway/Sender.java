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
package org.eclipse.kapua.client.gateway;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

/**
 * An interface to publish data
 *
 * @param <X>
 *            base class of sender errors
 */
public interface Sender<X extends Throwable> {

    public void send(Payload payload) throws X;

    public default void send(final Payload.Builder payload) throws X {
        send(payload.build());
    }

    public default <Y extends Throwable> Sender<Y> errors(final ErrorHandler<Y> errorHandler) {
        return new Sender<Y>() {

            @Override
            public void send(final Payload payload) throws Y {
                requireNonNull(payload);

                try {
                    Sender.this.send(payload);
                } catch (final Throwable e) {
                    errorHandler.handleError(e, Optional.of(payload));
                }
            }
        };

    }
}
