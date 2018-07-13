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
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

/**
 * Executes an event bus request/response interaction. Accepts a request 
 * as a JSON object and asynchronously returns a JSON object with the response.
 * <p>
 * Implementation classes have to check that JSON objects are valid.
 *
 */
public interface EBRequestHandler {

    public void handle(JsonObject request, Handler<AsyncResult<JsonObject>> response);
}
