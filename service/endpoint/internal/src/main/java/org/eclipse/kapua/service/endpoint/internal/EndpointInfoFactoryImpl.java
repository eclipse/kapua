/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.endpoint.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.endpoint.EndpointInfo;
import org.eclipse.kapua.service.endpoint.EndpointInfoCreator;
import org.eclipse.kapua.service.endpoint.EndpointInfoFactory;
import org.eclipse.kapua.service.endpoint.EndpointInfoListResult;
import org.eclipse.kapua.service.endpoint.EndpointInfoQuery;
import org.eclipse.kapua.service.endpoint.EndpointUsage;

/**
 * {@link EndpointInfoFactory} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class EndpointInfoFactoryImpl implements EndpointInfoFactory {

    @Override
    public EndpointInfo newEntity(KapuaId scopeId) {
        return new EndpointInfoImpl(scopeId);
    }

    @Override
    public EndpointInfoListResult newListResult() {
        return new EndpointInfoListResultImpl();
    }

    @Override
    public EndpointInfoQuery newQuery(KapuaId scopeId) {
        return new EndpointInfoQueryImpl(scopeId);
    }

    @Override
    public EndpointInfoCreator newCreator(KapuaId scopeId) {
        return new EndpointInfoCreatorImpl(scopeId);
    }

    @Override
    public EndpointUsage newEndpointUsage(String name) {
        return new EndpointUsageImpl(name);
    }
}
