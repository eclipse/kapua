/*******************************************************************************
 * Copyright (c) 2018 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.hono.services;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.proton.ProtonClientOptions;
import io.vertx.proton.ProtonConnection;
import io.vertx.proton.ProtonDelivery;
import org.apache.qpid.proton.amqp.Symbol;
import org.apache.qpid.proton.message.Message;
import org.eclipse.hono.client.CommandClient;
import org.eclipse.hono.client.CredentialsClient;
import org.eclipse.hono.client.HonoClient;
import org.eclipse.hono.client.MessageConsumer;
import org.eclipse.hono.client.MessageSender;
import org.eclipse.hono.client.RegistrationClient;
import org.eclipse.hono.client.TenantClient;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class KapuaHonoClient implements HonoClient {
    @Override
    public Future<Void> isConnected() {
        return null;
    }

    @Override
    public Future<HonoClient> connect() {
        return null;
    }

    @Override
    public Future<HonoClient> connect(ProtonClientOptions options) {
        return null;
    }

    @Override
    public Future<HonoClient> connect(Handler<ProtonConnection> disconnectHandler) {
        return null;
    }

    @Override
    public Future<HonoClient> connect(ProtonClientOptions options, Handler<ProtonConnection> disconnectHandler) {
        return null;
    }

    @Override
    public Future<MessageSender> getOrCreateTelemetrySender(String tenantId) {
        return null;
    }

    @Override
    public Future<MessageSender> getOrCreateTelemetrySender(String tenantId, String deviceId) {
        return null;
    }

    @Override
    public Future<MessageSender> getOrCreateEventSender(String tenantId) {
        return null;
    }

    @Override
    public Future<MessageSender> getOrCreateEventSender(String tenantId, String deviceId) {
        return null;
    }

    @Override
    public Future<MessageConsumer> createTelemetryConsumer(String tenantId, Consumer<Message> telemetryConsumer, Handler<Void> closeHandler) {
        return null;
    }

    @Override
    public Future<MessageConsumer> createEventConsumer(String tenantId, Consumer<Message> eventConsumer, Handler<Void> closeHandler) {
        return null;
    }

    @Override
    public Future<MessageConsumer> createEventConsumer(String tenantId, BiConsumer<ProtonDelivery, Message> eventConsumer, Handler<Void> closeHandler) {
        return null;
    }

    @Override
    public Future<RegistrationClient> getOrCreateRegistrationClient(String tenantId) {
        Objects.requireNonNull(tenantId);
        final Future<RegistrationClient> result = Future.future();
        result.complete(new KapuaRegistrationClient(tenantId));

        return result;
    }

    @Override
    public Future<CredentialsClient> getOrCreateCredentialsClient(String tenantId) {
        Objects.requireNonNull(tenantId);
        final Future<CredentialsClient> result = Future.future();
        result.complete(new KapuaCredentialsClient(tenantId));

        return result;
    }

    @Override
    public Future<TenantClient> getOrCreateTenantClient() {
        final Future<TenantClient> result = Future.future();
        result.complete(new KapuaTenantClient());

        return result;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void shutdown(Handler<AsyncResult<Void>> completionHandler) {

    }

    @Override
    public boolean supportsCapability(Symbol capability) {
        return false;
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void disconnect(Handler<AsyncResult<Void>> handler) {

    }

    @Override
    public Future<CommandClient> getOrCreateCommandClient(String s, String s1) {
        return null;
    }

    @Override
    public Future<CommandClient> getOrCreateCommandClient(String tenantId, String deviceId, String replyId) {
        return null;
    }
}
