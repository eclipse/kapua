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

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class HttpResponseHandler {

    private HttpResponseHandler() {
    }

    public static <T> Handler<AsyncResult<T>> httpRequestHandler(RoutingContext ctx) {
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

}
