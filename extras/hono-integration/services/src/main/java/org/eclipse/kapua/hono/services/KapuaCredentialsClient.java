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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import org.eclipse.hono.client.CredentialsClient;
import org.eclipse.hono.client.ServiceInvocationException;
import org.eclipse.hono.client.StatusCodeMapper;
import org.eclipse.hono.service.credentials.CredentialsService;
import org.eclipse.hono.util.CredentialsObject;
import org.eclipse.hono.util.CredentialsResult;

import java.io.IOException;
import java.net.HttpURLConnection;

public class KapuaCredentialsClient implements CredentialsClient {

    CredentialsService credentialsService;
    String tenantId = "kapua-sys";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public KapuaCredentialsClient(String tenantId) {
        this.tenantId = tenantId;
        credentialsService = new KapuaCredentialsService();
    }

    @Override
    public Future<CredentialsObject> get(String type, String authId) {
        return get(type, authId, new JsonObject());
    }

    @Override
    public Future<CredentialsObject> get(String type, String authId, JsonObject clientContext) {
        final Future<CredentialsResult<JsonObject>> result = Future.future();

        credentialsService.get(tenantId, type, authId, clientContext, result.completer());
        return result.map(response -> {
            switch(response.getStatus()) {
                case HttpURLConnection.HTTP_OK:
                    try {
                        return OBJECT_MAPPER.readValue(response.getPayload().toString(), CredentialsObject.class);
                    } catch (IOException ioe) {
                        throw new ServiceInvocationException(500);
                    }
                default:
                    throw StatusCodeMapper.from(response);
            }
        });
    }

    @Override
    public void close(Handler<AsyncResult<Void>> closeHandler) {

    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public void setRequestTimeout(long timoutMillis) {

    }
}
