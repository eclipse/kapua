/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.event.service.internal;

import org.eclipse.kapua.commons.event.service.api.ServiceEvent;
import org.eclipse.kapua.commons.event.service.api.ServiceEventCreator;
import org.eclipse.kapua.commons.event.service.api.ServiceEventListResult;
import org.eclipse.kapua.commons.event.service.api.ServiceEventStoreFactory;
import org.eclipse.kapua.commons.event.service.api.ServiceEventStoreQuery;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;

@KapuaProvider
public class ServiceEventStoreFactoryImpl implements ServiceEventStoreFactory {

    @Override
    public ServiceEvent newEntity(KapuaId scopeId) {
        return new ServiceEventImpl(scopeId);
    }

    @Override
    public ServiceEventCreator newCreator(KapuaId scopeId) {
        return new ServiceEventCreatorImpl(scopeId);
    }

    @Override
    public ServiceEventStoreQuery newQuery(KapuaId scopeId) {
        return new ServiceEventStoreQueryImpl(scopeId);
    }

    @Override
    public ServiceEventListResult newListResult() {
        return new ServiceEventListResultImpl();
    }
}