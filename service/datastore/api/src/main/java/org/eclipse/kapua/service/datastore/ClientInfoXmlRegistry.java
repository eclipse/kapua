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
package org.eclipse.kapua.service.datastore;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.datastore.model.ClientInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;

/**
 * Client information xml registry
 *
 * @since 1.0
 */
@XmlRegistry
public class ClientInfoXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DatastoreObjectFactory factory = locator.getFactory(DatastoreObjectFactory.class);

    /**
     * Creates a {@link ClientInfoListResult} instance
     * 
     * @return
     */
    public ClientInfoListResult newClientInfoListResult() {
        return factory.newClientInfoListResult();
    }

    /**
     * Creates a {@link ClientInfoQuery} instance.
     * 
     * @return
     */
    public ClientInfoQuery newQuery() {
        return factory.newClientInfoQuery(null);
    }
}
