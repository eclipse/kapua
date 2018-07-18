/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.core.vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

public abstract class AbstractEBClient {

    protected abstract EventBusProvider eventBusProvider();

    public abstract String getSendAddress();

    public void sendRequest(EBRequest request, Handler<AsyncResult<JsonObject>> result) {
        eventBusProvider().get().send(getSendAddress(), request, reqRes -> {
            if (reqRes.succeeded()) {
                JsonObject response = (JsonObject) reqRes.result().body();
                int resultCode = response.getInteger(EBResponse.STATUS_CODE);
                JsonObject body = response.getJsonObject(EBResponse.BODY);
                if ( resultCode == EBResponse.OK) {
                    result.handle(Future.succeededFuture(body));
                } else {
                    result.handle(Future.failedFuture(new EBResponseException(resultCode, body.getString("reply-msg"))));
                }
            } else {
                result.handle(Future.failedFuture(reqRes.cause()));
            }
        });
    }
}
