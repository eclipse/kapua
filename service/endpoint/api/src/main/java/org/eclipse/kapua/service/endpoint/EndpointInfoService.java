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
package org.eclipse.kapua.service.endpoint;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaDomainService;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;

/**
 * {@link EndpointInfo} service definition.
 *
 * @since 1.0.0
 */
public interface EndpointInfoService extends KapuaEntityService<EndpointInfo, EndpointInfoCreator>,
        KapuaUpdatableEntityService<EndpointInfo>,
        KapuaDomainService<EndpointInfoDomain> {

    public static final EndpointInfoDomain ENDPOINT_INFO_DOMAIN = new EndpointInfoDomain();

    @Override
    public default EndpointInfoDomain getServiceDomain() {
        return ENDPOINT_INFO_DOMAIN;
    }

    /**
     * Returns the {@link EndpointInfoListResult} with elements matching the provided query.
     *
     * @param query The {@link EndpointInfoQuery} used to filter results.
     * @return The {@link EndpointInfoListResult} with elements matching the query parameter.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    public EndpointInfoListResult query(KapuaQuery<EndpointInfo> query)
            throws KapuaException;
}
