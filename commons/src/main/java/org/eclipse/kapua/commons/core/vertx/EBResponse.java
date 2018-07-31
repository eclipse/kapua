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

import io.vertx.core.json.JsonObject;

/**
 * An event bus response used to answer a matching event bus request
 * within an event bus request/response interaction.
 *
 */
public class EBResponse extends JsonObject {

    public static final String STATUS_CODE = "status-code";
    public static final String BODY = "body";

    public static final int OK = 200;
    public static final int BAD_REQUEST = 400;
    public static final int NOT_FOUND = 404;
    public static final int INTERNAL_ERROR = 500;

    public static EBResponse create(int resultCode, JsonObject body) {
        EBResponse res = new EBResponse();
        res.setResultCode(resultCode);
        if (body != null) {
            res.setBody(body);
        }
        return res;
    }

    public static EBResponse create(int resultCode) {
        EBResponse res = new EBResponse();
        res.setResultCode(resultCode);
        return res;
    }

    public int getResultCode() {
        return this.getInteger(STATUS_CODE);
    }

    public void setResultCode(int resultCode) {
        this.put(STATUS_CODE, resultCode);
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
