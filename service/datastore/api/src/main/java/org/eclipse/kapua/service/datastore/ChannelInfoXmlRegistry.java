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

import javax.inject.Inject;
import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.service.datastore.model.ChannelInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;

/**
 * Channel information xml registry
 *
 * @since 1.0
 */
@XmlRegistry
public class ChannelInfoXmlRegistry {

    @Inject
    private DatastoreObjectFactory factory;

    /**
     * Creates a {@link ChannelInfoListResult} instance
     * 
     * @return
     */
    public ChannelInfoListResult newChannelInfoListResult() {
        return factory.newChannelInfoListResult();
    }

    /**
     * Creates a {@link ChannelInfoQuery} instance.
     * 
     * @return
     */
    public ChannelInfoQuery newQuery() {
        return factory.newChannelInfoQuery(null);
    }
}
