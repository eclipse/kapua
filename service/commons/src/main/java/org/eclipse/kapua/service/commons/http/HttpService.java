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

import java.util.Set;

import org.eclipse.kapua.service.commons.HealthCheckProvider;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

public interface HttpService extends HealthCheckProvider {

    public HttpService addControllers(Set<HttpController> someControllers);

    public HttpService addController(HttpController aController);

    public HttpService setAuthHandler(Handler<RoutingContext> anHandler);

    public void start(Future<Void> startFuture) throws Exception;

    public void stop(Future<Void> stopFuture) throws Exception;

    static HttpService create(Vertx vertx) {
        return new HttpServiceImpl(vertx);
    }

    static HttpService create(Vertx vertx, HttpServiceConfig config) {
        return new HttpServiceImpl(vertx, config);
    }
}
