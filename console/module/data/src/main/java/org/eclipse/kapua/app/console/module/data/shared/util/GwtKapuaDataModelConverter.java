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
package org.eclipse.kapua.app.console.module.data.shared.util;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDataChannelInfoQuery;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.datastore.DatastoreObjectFactory;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;

public class GwtKapuaDataModelConverter {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final DatastoreObjectFactory DATASTORE_FACTORY = LOCATOR.getFactory(DatastoreObjectFactory.class);

    private GwtKapuaDataModelConverter() {
    }

    public static ChannelInfoQuery convertChannelInfoQuery(GwtDataChannelInfoQuery query, PagingLoadConfig pagingLoadConfig) {
        ChannelInfoQuery channelInfoQuery = DATASTORE_FACTORY.newChannelInfoQuery(GwtKapuaCommonsModelConverter.convertKapuaId(query.getScopeId()));
        channelInfoQuery.setOffset(pagingLoadConfig.getOffset());
        channelInfoQuery.setLimit(pagingLoadConfig.getLimit());
        return channelInfoQuery;
    }
}
