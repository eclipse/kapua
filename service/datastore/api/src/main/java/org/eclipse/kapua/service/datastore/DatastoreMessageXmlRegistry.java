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
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;

@XmlRegistry
public class DatastoreMessageXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DatastoreObjectFactory factory = locator.getFactory(DatastoreObjectFactory.class);

    /**
     * Creates a {@link MessageListResult} instance
     * 
     * @return
     */
    public MessageListResult newDatastoreMessageListResult() {
        return factory.newDatastoreMessageListResult();
    }

    /**
     * Creates a {@link MessageQuery} instance.
     * 
     * @return
     */
    public MessageQuery newQuery() {
        return factory.newDatastoreMessageQuery(null);
    }
}
