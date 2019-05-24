/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.commons.http;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;

public class HttpServiceHandlers {

    private static final KapuaLocator KAPUA_LOCATOR = KapuaLocator.getInstance();
    private static final CredentialsFactory CREDENTIALS_FACTORY = KAPUA_LOCATOR.getFactory(CredentialsFactory.class);
    private static final AuthenticationService AUTHENTICATION_SERVICE = KAPUA_LOCATOR.getService(AuthenticationService.class);

    private HttpServiceHandlers() {
    }

    public static <T> Handler<AsyncResult<T>> httpResponseHandler(RoutingContext ctx) {
        return result -> {
            if (result.succeeded()) {
                ctx.response().setStatusCode(200);
                if (result.result() != null) {
                    ctx.response().setChunked(true).write(Json.encode(result.result()));
                }
                ctx.response().end();
            } else {
                ctx.fail(500, result.cause());
            }
        };
    }

    public static Handler<RoutingContext> authenticationHandler() {
        return HttpServiceHandlers::authenticationHandler;
    }

    public static void authenticationHandler(RoutingContext ctx) {
        String accessToken = StringUtils.removeStart(ctx.request().getHeader("Authorization"), "Bearer ");
        AccessTokenCredentials accessTokenCredentials = CREDENTIALS_FACTORY.newAccessTokenCredentials(accessToken);
        try {
            AUTHENTICATION_SERVICE.authenticate(accessTokenCredentials);
        } catch (KapuaException ex) {
            ctx.fail(ex);
        }
        ctx.put("kapuaSession", KapuaSecurityUtils.getSession());
        ctx.put("shiroSubject", SecurityUtils.getSubject());
        ctx.next();
    }

    public static Handler<RoutingContext> failureHandler() {
        return HttpServiceHandlers::failureHandler;
    }

    public static void failureHandler(RoutingContext ctx) {
            JsonObject error = new JsonObject();
            error.put("error", ctx.failure().getMessage());
            ctx.response().setStatusCode(ctx.statusCode() == -1 ? 500 : ctx.statusCode()).end(error.encode());
    }

}
