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
package org.eclipse.kapua.microservice.commons;

import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaIdFactory;
import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public abstract class AbstractHttpServerVerticle extends AbstractVerticle {

    private final KapuaLocator kapuaLocator = KapuaLocator.getInstance();
    private final KapuaIdFactory kapuaIdFactory = kapuaLocator.getFactory(KapuaIdFactory.class);
    private final CredentialsFactory credentialsFactory = kapuaLocator.getFactory(CredentialsFactory.class);
    private final AuthenticationService authenticationService = kapuaLocator.getService(AuthenticationService.class);

    protected abstract List<HttpEndpoint> getHttpEndpoint();

    protected HttpServerOptions getHttpServerOptions() {
        return new HttpServerOptions();
    }

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        // Login
        router.route().blockingHandler(ctx -> {
//            AccessTokenCredentials accessTokenCredentials = credentialsFactory.newAccessTokenCredentials(ctx.request().getHeader("X-Access-Token"));
//            try {
//                authenticationService.authenticate(accessTokenCredentials);
//            } catch (KapuaException ex) {
//                ctx.fail(ex);
//            }
            KapuaSecurityUtils.setSession(new KapuaSession());
            ctx.put("kapuaSession", KapuaSecurityUtils.getSession());
            ctx.put("threadContext", vertx.getOrCreateContext());
            ctx.next();
        });
        // TODO Put Service Event

        // Put ScopeId and JobId in context if found
        router.routeWithRegex("\\/(?<resourceName>[a-zA-Z0-9_-]+)\\/(?<scopeId>[a-zA-Z0-9_-]+)\\/(?<jobId>[a-zA-Z0-9_-]+)").handler(ctx -> {
            String scopeId = ctx.request().getParam("scopeId");
            String jobId = ctx.request().getParam("jobId");
            if (scopeId != null) {
                ctx.put("scopeId", kapuaIdFactory.newKapuaId(scopeId));
            }
            if (jobId != null) {
                ctx.put("jobId", kapuaIdFactory.newKapuaId(jobId));
            }
            ctx.next();
        });

        // Register child routes
        getHttpEndpoint().forEach(endpoint -> endpoint.registerRoutes(router));

        // Logout
        router.route().blockingHandler(ctx -> KapuaSecurityUtils.clearSession());
        vertx.createHttpServer(getHttpServerOptions()).requestHandler(router).listen();
    }

}
