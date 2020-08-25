/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.model.KapuaEntityFactory;

/**
 * {@link EndpointInfoFactory} definition.
 *
 * @since 1.0.0
 */
public interface EndpointInfoFactory extends KapuaEntityFactory<EndpointInfo, EndpointInfoCreator, EndpointInfoQuery, EndpointInfoListResult> {

    /**
     * Instantiates a new {@link EndpointUsage}.
     *
     * @param name The name to set into the {@link EndpointUsage}.
     * @return The newly instantiated {@link EndpointUsage}.
     * @since 1.0.0
     */
    EndpointUsage newEndpointUsage(String name);
}
