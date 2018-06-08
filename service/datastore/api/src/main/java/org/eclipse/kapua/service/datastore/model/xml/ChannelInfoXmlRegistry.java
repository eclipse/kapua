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
import org.eclipse.kapua.service.datastore.model.ChannelInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * Channel information xml registry
 *
 * @since 1.0
 */
@XmlRegistry
public class ChannelInfoXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DatastoreObjectFactory DATASTORE_OBJECT_FACTORY = LOCATOR.getFactory(DatastoreObjectFactory.class);

    /**
     * Creates a {@link ChannelInfoListResult} instance
     *
     * @return
     */
    public ChannelInfoListResult newChannelInfoListResult() {
        return DATASTORE_OBJECT_FACTORY.newChannelInfoListResult();
    }

    /**
     * Creates a {@link ChannelInfoQuery} instance.
     *
     * @return
     */
    public ChannelInfoQuery newQuery() {
        return DATASTORE_OBJECT_FACTORY.newChannelInfoQuery(null);
    }
}
