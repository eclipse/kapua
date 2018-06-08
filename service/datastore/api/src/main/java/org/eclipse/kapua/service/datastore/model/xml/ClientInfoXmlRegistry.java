/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.model.xml;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.datastore.DatastoreObjectFactory;
import org.eclipse.kapua.service.datastore.model.ClientInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * Client information xml registry
 *
 * @since 1.0
 */
@XmlRegistry
public class ClientInfoXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DatastoreObjectFactory DATASTORE_OBJECT_FACTORY = LOCATOR.getFactory(DatastoreObjectFactory.class);

    /**
     * Creates a {@link ClientInfoListResult} instance
     *
     * @return
     */
    public ClientInfoListResult newClientInfoListResult() {
        return DATASTORE_OBJECT_FACTORY.newClientInfoListResult();
    }

    /**
     * Creates a {@link ClientInfoQuery} instance.
     *
     * @return
     */
    public ClientInfoQuery newQuery() {
        return DATASTORE_OBJECT_FACTORY.newClientInfoQuery(null);
    }
}
