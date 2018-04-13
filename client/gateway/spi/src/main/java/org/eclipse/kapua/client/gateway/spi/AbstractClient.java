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

import org.eclipse.kapua.client.gateway.Application;
import org.eclipse.kapua.client.gateway.Client;
import org.eclipse.kapua.client.gateway.ErrorHandler;
import org.eclipse.kapua.client.gateway.MessageHandler;
import org.eclipse.kapua.client.gateway.Payload;
import org.eclipse.kapua.client.gateway.Topic;
import org.eclipse.kapua.client.gateway.Transport;
import org.eclipse.kapua.client.gateway.spi.util.Futures;
import org.eclipse.kapua.client.gateway.spi.util.TransportAsync;
import org.eclipse.kapua.client.gateway.spi.util.TransportProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

public abstract class AbstractClient implements Client {

    private static final Logger logger = LoggerFactory.getLogger(AbstractClient.class);

    private class ContextImpl implements Context {

        private final String applicationId;

        private final Set<Topic> subscriptions = new HashSet<>();

        private final TransportProxy transport;

        private ContextImpl(String applicationId) {
            this.applicationId = applicationId;
            transport = TransportProxy.proxy(AbstractClient.this.transport, executor);
        }

        @Override
        public String getId() {
            return applicationId;
        }

        @Override
        public CompletionStage<?> publish(final Topic topic, final Payload payload) {
            return internalPublish(this, applicationId, topic, payload);
        }

        @Override
        public CompletionStage<?> subscribe(Topic topic, MessageHandler messageHandler, ErrorHandler<? extends Throwable> errorHandler) {
            return internalSubscribe(this, applicationId, topic, messageHandler, errorHandler);
        }

        @Override
        public Transport transport() {
            return transport;
        }

        @Override
        public void close() {
            try {
                internalCloseApplication(this, applicationId);
            } finally {
                transport.close();
            }
        }

        protected Set<Topic> getSubscriptions() {
            return subscriptions;
        }
    }

    public static abstract class Builder<T extends Builder<T>> implements Client.Builder {

        protected abstract T builder();

        private final Set<Module> modules = new HashSet<>();

        public T module(final Module module) {
            Objects.requireNonNull(module);

            this.modules.add(module);
            return builder();
        }

        public Set<Module> modules() {
            return this.modules;
        }
    }

    public interface Context {

        public String getId();

        public CompletionStage<?> publish(Topic topic, Payload payload);

        public CompletionStage<?> subscribe(Topic topic, MessageHandler handler, ErrorHandler<? extends Throwable> errorHandler);

        public void close();

        public Transport transport();
    }

    protected final ScheduledExecutorService executor;
    private final Set<Module> modules;

    private final TransportAsync transport;

    private final Map<String, Context> applications = new HashMap<>();

    public AbstractClient(final ScheduledExecutorService executor, final Set<Module> modules) {
        this.executor = executor;
        this.modules = new HashSet<>(modules);

        transport = new TransportAsync(executor);
    }

    protected void init() {
        fireModuleEvent(module -> module.initialize(new ModuleContext() {

            @Override
            public Client getClient() {
                return AbstractClient.this;
            }

            @Override
            public <T> Optional<T> adapt(final Class<T> clazz) {
                Objects.requireNonNull(clazz);

                return adaptModuleContext(clazz);
            }
        }));
    }

    protected abstract CompletionStage<?> handleSubscribe(String applicationId, Topic topic, MessageHandler messageHandler, ErrorHandler<? extends Throwable> errorHandler);

    protected abstract CompletionStage<?> handlePublish(String applicationId, Topic topic, Payload payload);

    protected abstract void handleUnsubscribe(String applicationId, Collection<Topic> topics) throws Exception;

    protected <T> Optional<T> adaptModuleContext(final Class<T> clazz) {
        return Optional.empty();
    }

    @Override
    public Transport transport() {
        return transport;
    }

    private void fireModuleEvent(final Consumer<Module> consumer) {
        for (final Module module : modules) {
            try {
                consumer.accept(module);
            } catch (final Exception e) {
                logger.info("Failed to process module event", e);
            }
        }
    }

    protected void notifyAddApplication(final String applicationId) {
        fireModuleEvent(module -> module.applicationAdded(applicationId));
    }

    protected void notifyRemoveApplication(final String applicationId) {
        fireModuleEvent(module -> module.applicationRemoved(applicationId));
    }

    protected void notifyConnected() {
        fireModuleEvent(Module::connected);
        transport.handleConnected();
    }

    protected void notifyDisconnected() {
        fireModuleEvent(Module::disconnected);
        transport.handleDisconnected();
    }

    protected void handleConnected() {
        logger.info("Connected");

        notifyConnected();
    }

    protected void handleDisconnected() {
        logger.info("Disconnected");

        notifyDisconnected();
    }

    @Override
    public Application.Builder buildApplication(final String applicationId) {
        return new Application.Builder() {

            @Override
            public Application build() {
                return internalBuildApplication(this, applicationId);
            }
        };
    }

    protected DefaultApplication internalBuildApplication(final Application.Builder builder, final String applicationId) {
        synchronized (this) {
            if (applications.containsKey(applicationId)) {
                throw new IllegalStateException(String.format("An application with the ID '%s' already exists", applicationId));
            }

            final Context context = new ContextImpl(applicationId);

            final DefaultApplication result = createApplication(builder, context);

            applications.put(applicationId, context);
            notifyAddApplication(applicationId);

            return result;
        }
    }

    protected synchronized CompletionStage<?> internalSubscribe(final ContextImpl context, final String applicationId, final Topic topic, final MessageHandler messageHandler,
                                                                final ErrorHandler<? extends Throwable> errorHandler) {
        if (applications.get(applicationId) != context) {
            return Futures.completedExceptionally(new IllegalStateException(String.format("Application '%s' is already closed", applicationId)));
        }

        if (!context.getSubscriptions().add(topic)) {
            return CompletableFuture.completedFuture(null);
        }

        return handleSubscribe(applicationId, topic, messageHandler, errorHandler);
    }

    protected synchronized CompletionStage<?> internalPublish(final Context context, final String applicationId, final Topic topic, final Payload payload) {
        if (applications.get(applicationId) != context) {
            return Futures.completedExceptionally(new IllegalStateException(String.format("Application '%s' is already closed", applicationId)));
        }

        return handlePublish(applicationId, topic, payload);
    }

    protected DefaultApplication createApplication(final Application.Builder builder, final AbstractClient.Context context) {
        return new DefaultApplication(context);
    }

    protected synchronized void internalCloseApplication(final ContextImpl context, final String applicationId) {
        if (applications.remove(applicationId, context)) {
            try {
                handleUnsubscribe(applicationId, context.getSubscriptions());
            } catch (Exception e) {
                logger.warn("Failed to unsubscribe on application close", e);
            }
            notifyRemoveApplication(applicationId);
        }
    }
}
