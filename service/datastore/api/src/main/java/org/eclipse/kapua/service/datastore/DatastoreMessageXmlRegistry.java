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

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * Datastore message xml registry
 *
 * @since 1.0
 */
@XmlRegistry
public class DatastoreMessageXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DatastoreObjectFactory DATASTORE_OBJECT_FACTORY = LOCATOR.getFactory(DatastoreObjectFactory.class);

    /**
     * Creates a {@link MessageListResult} instance
     *
     * @return
     */
    public MessageListResult newDatastoreMessageListResult() {
        return DATASTORE_OBJECT_FACTORY.newDatastoreMessageListResult();
    }

    /**
     * Creates a {@link MessageQuery} instance.
     *
     * @return
     */
    public MessageQuery newQuery() {
        return DATASTORE_OBJECT_FACTORY.newDatastoreMessageQuery(null);
    }
}
