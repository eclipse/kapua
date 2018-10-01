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

import java.util.Objects;

import io.vertx.core.json.JsonObject;

/**
 * An event bus request used to trigger an event bus request/response
 * interaction.
 * <p>
 * The action is a string in dotted notation (e.g. "service.method")
 *
 */
public class EventBusServerRequest extends JsonObject {

    public static final String ACTION = "action";
    public static final String BODY = "body";

    public static EventBusServerRequest create(String action, JsonObject body) {
        EventBusServerRequest req = new EventBusServerRequest();
        req.setAction(action);
        if (body != null) {
            req.setBody(body);
        }
        return req;
    }

    public static EventBusServerRequest create(JsonObject request) {
        Objects.requireNonNull(request, "Invalid null request");
        EventBusServerRequest req = new EventBusServerRequest();
        if (!request.containsKey(ACTION) || !(request.getValue(ACTION) instanceof String)) {
            throw new RuntimeException("Request Action is not a string");
        }
        req.setAction(request.getString(ACTION));
        if (!request.containsKey(BODY) || request.getValue(BODY) == null || !(request.getValue(BODY) instanceof JsonObject)) {
            throw new RuntimeException("Request Body is not a json object");
        }
        req.setBody((JsonObject) request.getValue(BODY));
        return req;
    }

    public String getAction() {
        return this.getString(ACTION);
    }

    public void setAction(String action) {
        this.put(ACTION, action);
    }

    public JsonObject getBody() {
        return this.getJsonObject(BODY);
    }

    public void setBody(JsonObject body) {
        this.put(BODY, body);
    }

    public boolean hasBody() {
        return (this.getBody() == null);
    }
}
