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

import io.vertx.core.eventbus.EventBus;

/**
 * Defines the interface for a service that implements a request/response 
 * interaction through the {@link EventBus}.
 * <p>
 * It can register a set of {@link EventBusServiceAdapter}, handlers will 
 * be invoked when a matching {@link EventBusServerRequest} is received.
 */
public interface EventBusService extends LifecycleObject {

    public void register(EventBusServiceAdapter provider);

    public void register(HealthCheckAdapter provider);
}
