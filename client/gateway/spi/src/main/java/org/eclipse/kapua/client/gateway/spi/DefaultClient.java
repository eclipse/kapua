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

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.eclipse.kapua.client.gateway.Client;
import org.eclipse.kapua.client.gateway.ErrorHandler;
import org.eclipse.kapua.client.gateway.MessageHandler;
import org.eclipse.kapua.client.gateway.Payload;
import org.eclipse.kapua.client.gateway.Topic;
import org.eclipse.kapua.client.gateway.spi.util.MoreExecutors;

public class DefaultClient extends AbstractClient {

    public static final class Builder extends AbstractClient.Builder<Builder> {

        private final Channel channel;

        public Builder(final Channel channel) {
            Objects.requireNonNull(channel);

            this.channel = channel;
        }

        protected Builder builder() {
            return this;
        }

        @Override
        public Client build() throws Exception {
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

            try {
                final DefaultClient client = new DefaultClient(channel, executor, modules());
                try {
                    client.init();
                } catch (Exception e) {
                    // init failed, close
                    client.close();
                    // and rethrow
                    throw e;
                }

                // all good, claim instance
                executor = null;

                // and return
                return client;
            } finally {
                if (executor != null) {
                    // not claime -> dispose
                    executor.shutdown();
                }
            }
        }
    }

    private final Channel.Context context = new Channel.Context() {

        @Override
        public void notifyConnected() {
            DefaultClient.this.notifyConnected();
        }

        @Override
        public void notifyDisconnected() {
            DefaultClient.this.notifyDisconnected();
        }

        @Override
        public ScheduledExecutorService executor() {
            return MoreExecutors.preventShutdown(executor);
        }

    };

    private final Channel channel;

    public DefaultClient(final Channel channel, final ScheduledExecutorService executor, final Set<Module> modules) {
        super(executor, modules);

        Objects.requireNonNull(channel);
        this.channel = channel;
        this.channel.handleInit(context);
    }

    @Override
    public void close() throws Exception {
        channel.handleClose(context);
        executor.shutdown();
    }

    @Override
    protected <T> Optional<T> adaptModuleContext(final Class<T> clazz) {

        final Optional<T> result = channel.adapt(clazz);
        if (result.isPresent()) {
            return result;
        }

        return super.adaptModuleContext(clazz);
    }

    @Override
    protected CompletionStage<?> handleSubscribe(String applicationId, Topic topic, MessageHandler messageHandler, ErrorHandler<? extends Throwable> errorHandler) {
        return channel.handleSubscribe(applicationId, topic, messageHandler, errorHandler);
    }

    @Override
    protected CompletionStage<?> handlePublish(String applicationId, Topic topic, Payload payload) {
        return channel.handlePublish(applicationId, topic, payload);
    }

    @Override
    protected void handleUnsubscribe(String applicationId, Collection<Topic> topics) throws Exception {
        channel.handleUnsubscribe(applicationId, topics);
    }

}
