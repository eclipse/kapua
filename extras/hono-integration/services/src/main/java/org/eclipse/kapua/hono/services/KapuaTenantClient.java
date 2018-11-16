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
import org.eclipse.hono.client.ServiceInvocationException;
import org.eclipse.hono.client.StatusCodeMapper;
import org.eclipse.hono.client.TenantClient;
import org.eclipse.hono.service.tenant.TenantService;
import org.eclipse.hono.util.TenantObject;
import org.eclipse.hono.util.TenantResult;

import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.net.HttpURLConnection;

public class KapuaTenantClient implements TenantClient {

    TenantService tenantService = new KapuaTenantService();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public Future<TenantObject> get(String tenantId) {
        final Future<TenantResult<JsonObject>> result = Future.future();

        tenantService.get(tenantId, result.completer());

        return result.map(response -> {
            switch(response.getStatus()) {
                case HttpURLConnection.HTTP_OK:
                    try {
                        return OBJECT_MAPPER.readValue(response.getPayload().toString(), TenantObject.class);
                    } catch (IOException ioe) {
                        throw new ServiceInvocationException(500);
                    }
                default:
                    throw StatusCodeMapper.from(response);
            }
        });
    }

    @Override
    public Future<TenantObject> get(X500Principal subjectDn) {
        final Future<TenantResult<JsonObject>> result = Future.future();

        tenantService.get(subjectDn, result.completer());

        return result.map(response -> {
            switch(response.getStatus()) {
                case HttpURLConnection.HTTP_OK:
                    try {
                        return OBJECT_MAPPER.readValue(response.getPayload().toString(), TenantObject.class);
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
        return false;
    }

    @Override
    public void setRequestTimeout(long timoutMillis) {

    }
}
