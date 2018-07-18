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

public class EBRequest extends JsonObject {

    public static final String ACTION = "action";
    public static final String BODY = "body";

    public static EBRequest create(String action, JsonObject body) {
        EBRequest req = new EBRequest();
        req.setAction(action);
        if (body != null) {
            req.setBody(body);
        }
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
