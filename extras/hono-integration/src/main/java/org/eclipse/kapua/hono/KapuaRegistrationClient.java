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
package org.eclipse.kapua.hono;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import org.eclipse.hono.client.RegistrationClient;
import org.eclipse.hono.client.StatusCodeMapper;
import org.eclipse.hono.service.registration.RegistrationService;
import org.eclipse.hono.util.RegistrationResult;

import java.net.HttpURLConnection;

public class KapuaRegistrationClient implements RegistrationClient {

    RegistrationService registrationService = new KapuaRegistrationService();
    String tenantId;

    public KapuaRegistrationClient(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public Future<JsonObject> assertRegistration(String deviceId) {
        return assertRegistration(deviceId, null);
    }

    @Override
    public Future<JsonObject> assertRegistration(String deviceId, String gatewayId) {
        final Future<RegistrationResult> result = Future.future();

        registrationService.assertRegistration(tenantId, deviceId, gatewayId, result.completer());

        return result.map(response -> {
            switch(response.getStatus()) {
                case HttpURLConnection.HTTP_OK:
                    return response.getPayload();
                default:
                    throw StatusCodeMapper.from(response);
            }
        });
    }

    @Override
    public Future<JsonObject> get(String deviceId) {
        return null;
    }

    @Override
    public Future<Void> register(String deviceId, JsonObject data) {
        return null;
    }

    @Override
    public Future<Void> update(String deviceId, JsonObject data) {
        return null;
    }

    @Override
    public Future<Void> deregister(String deviceId) {
        return null;
    }

    @Override
    public void close(Handler<AsyncResult<Void>> closeHandler) {

    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void setRequestTimeout(long timoutMillis) {

    }
}
