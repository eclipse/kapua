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

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class EndpointInfoXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final EndpointInfoFactory FACTORY = LOCATOR.getFactory(EndpointInfoFactory.class);

    /**
     * Creates a new {@link EndpointInfo} instance
     *
     * @return
     */
    public EndpointInfo newEntity() {
        return FACTORY.newEntity(null);
    }

    /**
     * Creates a new {@link EndpointInfoCreator} instance
     *
     * @return
     */
    public EndpointInfoCreator newCreator() {
        return FACTORY.newCreator(null);
    }

    /**
     * Creates a new {@link EndpointInfoListResult}
     *
     * @return
     */
    public EndpointInfoListResult newListResult() {
        return FACTORY.newListResult();
    }

    /**
     * Creates a new {@link EndpointInfoQuery}
     *
     * @return
     */
    public EndpointInfoQuery newQuery() {
        return FACTORY.newQuery(null);
    }

    public EndpointUsage newEndpointUsage() {
        return FACTORY.newEndpointUsage(null);
    }
}
