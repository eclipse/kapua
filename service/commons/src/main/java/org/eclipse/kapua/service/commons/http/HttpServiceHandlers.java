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

import javax.validation.constraints.NotNull;
import java.util.Base64;
import java.util.Objects;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class HttpServiceHandlers {

    private static final KapuaLocator KAPUA_LOCATOR = KapuaLocator.getInstance();
    private static final CredentialsFactory CREDENTIALS_FACTORY = KAPUA_LOCATOR.getFactory(CredentialsFactory.class);
    private static final AuthenticationService AUTHENTICATION_SERVICE = KAPUA_LOCATOR.getService(AuthenticationService.class);

    private HttpServiceHandlers() {
    }

    public static <T> Handler<AsyncResult<T>> httpResponseHandler(@NotNull RoutingContext ctx) {
        Objects.requireNonNull(ctx, "param: ctx");
        return result -> {
            if (result.succeeded()) {
                if (result.result() != null) {
                    ctx.response().setStatusCode(200);
                    ctx.response().setChunked(true).write(Json.encode(result.result()));
                } else {
                    ctx.response().setStatusCode(204);
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

    public static void authenticationHandler(@NotNull RoutingContext ctx) {
        Objects.requireNonNull(ctx, "param: ctx");
        KapuaSession session = null;
        Subject subject = null;
        try {
            String sessionJson = new String(Base64.getDecoder().decode(ctx.request().getHeader("X-Kapua-Session")));
            JsonObject sessionJsonObject = (JsonObject)Json.decodeValue(sessionJson);
            sessionJsonObject.getJsonObject("accessToken").put("type", "accessToken");
            session = XmlUtil.unmarshalJson(sessionJsonObject.encode(), KapuaSession.class, null);
            if (!session.isTrustedMode()) {
                AccessTokenCredentials accessTokenCredentials = CREDENTIALS_FACTORY.newAccessTokenCredentials(session.getAccessToken().getTokenId());
                AUTHENTICATION_SERVICE.authenticate(accessTokenCredentials);
                subject = SecurityUtils.getSubject();
            }
            KapuaSecurityUtils.setSession(session);

        } catch (Exception e) {
            ctx.fail(500, e);
        }
        ctx.put("kapuaSession", session);
        ctx.put("shiroSubject", subject);
        ctx.next();
    }

    public static Handler<RoutingContext> failureHandler() {
        return HttpServiceHandlers::failureHandler;
    }

    public static void failureHandler(@NotNull RoutingContext ctx) {
        Objects.requireNonNull(ctx, "param: ctx");
        if (!ctx.response().ended()) {
            JsonObject error = new JsonObject();
            Throwable failure = ctx.failure();

            String errorCode;
            if (failure instanceof KapuaException) {
                errorCode = ((KapuaException)failure).getCode().name();
            } else if (failure instanceof KapuaRuntimeException) {
                errorCode = ((KapuaRuntimeException)failure).getCode().name();
            } else {
                errorCode = "UNKNOWN_ERROR";
            }

            error.put("message", ctx.failure().getMessage());
            error.put("errorCode", errorCode);
            ctx.response().setStatusCode(ctx.statusCode() == -1 ? 500 : ctx.statusCode()).end(error.encode());
        }
    }

}
