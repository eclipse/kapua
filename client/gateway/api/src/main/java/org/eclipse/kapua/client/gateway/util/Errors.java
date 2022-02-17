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
package org.eclipse.kapua.client.gateway.util;

import java.util.Optional;
import java.util.function.BiConsumer;

import org.eclipse.kapua.client.gateway.ErrorHandler;
import org.eclipse.kapua.client.gateway.Payload;

public final class Errors {

    private Errors() {
    }

    public static ErrorHandler<RuntimeException> ignore() {
        return Errors::ignore;
    }

    public static void ignore(final Throwable e, final Optional<Payload> payload) {
        // Ignore the error
    }

    public static ErrorHandler<RuntimeException> handle(final BiConsumer<Throwable, Optional<Payload>> handler) {
        return new ErrorHandler<RuntimeException>() {

            @Override
            public void handleError(final Throwable e, final Optional<Payload> payload) throws RuntimeException {
                handler.accept(e, payload);
            }
        };
    }
}
