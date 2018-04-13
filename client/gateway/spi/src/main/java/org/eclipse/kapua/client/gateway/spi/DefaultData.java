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
package org.eclipse.kapua.client.gateway.spi;

import java.util.Objects;
import java.util.concurrent.CompletionStage;

import org.eclipse.kapua.client.gateway.Data;
import org.eclipse.kapua.client.gateway.ErrorHandler;
import org.eclipse.kapua.client.gateway.MessageHandler;
import org.eclipse.kapua.client.gateway.Payload;
import org.eclipse.kapua.client.gateway.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultData implements Data {

    private static final Logger logger = LoggerFactory.getLogger(DefaultData.class);

    private final DefaultApplication application;
    private final Topic topic;

    public DefaultData(final DefaultApplication application, final Topic topic) {
        this.application = application;
        this.topic = topic;
    }

    @Override
    public CompletionStage<?> send(final Payload payload) {
        return application.publish(topic, payload);
    }

    @Override
    public CompletionStage<?> subscribe(final MessageHandler handler, final ErrorHandler<? extends Throwable> errorHandler) throws Exception {
        Objects.requireNonNull(handler);
        Objects.requireNonNull(errorHandler);

        logger.debug("Setting subscription for: {}", topic);

        return application.subscribe(topic, handler, errorHandler);
    }
}
