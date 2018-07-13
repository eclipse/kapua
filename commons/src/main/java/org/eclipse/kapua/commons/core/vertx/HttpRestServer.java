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

import io.vertx.core.Verticle;

/**
 * Defines the interface for a verticle exposing an http endpoint
 * <p>
 * It can register a set of {@link HttpRouteProvider}, routes will be invoked when 
 * a matching http request is received.
 * <p>
 * It can register a set of {@link HealthCheckProvider}, health checks will be 
 * invoked when an http request is received at a well defined path defined by 
 * its implementation.
 *
 */
public interface HttpRestServer extends Verticle {

    public void registerRouteProvider(HttpRouteProvider provider);

    public void registerHealthCheckProvider(HealthCheckProvider provider);
}
