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

/**
 * Defines the interface for a service exposing an http endpoint.
 * <p>
 * It can register a set of {@link HttpServiceAdapter}, routes will be invoked when 
 * a matching http request is received.
 * <p>
 * It can register a set of {@link HealthCheckAdapter}, health checks will be 
 * invoked when an http request is received at a well defined path defined by 
 * its implementation.
 *
 */
public interface HttpService extends LifecycleObject {

    public void register(HttpServiceAdapter provider);

    public void register(HealthCheckAdapter provider);
}
