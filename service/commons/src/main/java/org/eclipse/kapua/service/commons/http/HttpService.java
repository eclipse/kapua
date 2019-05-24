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

import io.vertx.core.Future;
import io.vertx.core.Vertx;

public interface HttpService extends HttpEndpointProvider {

    interface Builder {

        void setServiceConfig(HttpServiceConfig config);

        void addEndpoint(HttpEndpoint endpoint);

        HttpService build();
    }

    void start(Future<Void> startFuture) throws Exception;

    void stop(Future<Void> stopFuture) throws Exception;

    static Builder builder(Vertx vertx) {
        return new HttpServiceImpl.Builder(vertx);
    }

    static Builder builder(Vertx vertx, HttpServiceConfig config) {
        return new HttpServiceImpl.Builder(vertx, config);
    }
}
