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
package org.eclipse.kapua.client.gateway.util;

import java.util.Optional;
import java.util.function.BiConsumer;

import org.eclipse.kapua.client.gateway.ErrorHandler;
import org.eclipse.kapua.client.gateway.Payload;

public final class Errors {

    private static final ErrorHandler<RuntimeException> IGNORE = Errors::ignore;

    private Errors() {
    }

    public static ErrorHandler<RuntimeException> ignore() {
        return IGNORE;
    }

    public static ErrorHandler<RuntimeException> handle(final BiConsumer<Throwable, Optional<Payload>> handler) {
        return new ErrorHandler<RuntimeException>() {

            @Override
            public void handleError(final Throwable e, final Optional<Payload> payload) throws RuntimeException {
                handler.accept(e, payload);
            }
        };
    }

    public static void ignore(final Throwable e, final Optional<Payload> payload) {
    }
}
