/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.endpoint;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class EndpointInfoXmlRegistry {

    private final EndpointInfoFactory endpointInfoFactory = KapuaLocator.getInstance().getFactory(EndpointInfoFactory.class);

    /**
     * Creates a new {@link EndpointInfo} instance
     *
     * @return
     */
    public EndpointInfo newEntity() {
        return endpointInfoFactory.newEntity(null);
    }

    /**
     * Creates a new {@link EndpointInfoCreator} instance
     *
     * @return
     */
    public EndpointInfoCreator newCreator() {
        return endpointInfoFactory.newCreator(null);
    }

    /**
     * Creates a new {@link EndpointInfoListResult}
     *
     * @return
     */
    public EndpointInfoListResult newListResult() {
        return endpointInfoFactory.newListResult();
    }

    /**
     * Creates a new {@link EndpointInfoQuery}
     *
     * @return
     */
    public EndpointInfoQuery newQuery() {
        return endpointInfoFactory.newQuery(null);
    }

    public EndpointUsage newEndpointUsage() {
        return endpointInfoFactory.newEndpointUsage(null);
    }
}
