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
package org.eclipse.kapua.client.gateway.spi;

import java.util.concurrent.CompletionStage;

import org.eclipse.kapua.client.gateway.Application;
import org.eclipse.kapua.client.gateway.ErrorHandler;
import org.eclipse.kapua.client.gateway.MessageHandler;
import org.eclipse.kapua.client.gateway.Payload;
import org.eclipse.kapua.client.gateway.Topic;
import org.eclipse.kapua.client.gateway.Transport;

public class DefaultApplication implements Application {

    private final AbstractClient.Context context;

    public DefaultApplication(final AbstractClient.Context context) {
        this.context = context;
    }

    @Override
    public synchronized Transport transport() {
        return context.transport();
    }

    @Override
    public DefaultData data(Topic topic) {
        return new DefaultData(this, topic);
    }

    @Override
    public void close() throws Exception {
        context.close();
    }

    protected CompletionStage<?> publish(Topic topic, Payload payload) {
        return context.publish(topic, payload);
    }

    public CompletionStage<?> subscribe(final Topic topic, final MessageHandler handler, final ErrorHandler<? extends Throwable> errorHandler) throws Exception {
        return context.subscribe(topic, handler, errorHandler);
    }
}
