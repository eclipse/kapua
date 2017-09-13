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
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelInfoQueryImpl;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;

public class GwtKapuaDataModelConverter {

    private GwtKapuaDataModelConverter() { }

        public static ChannelInfoQuery convertChannelInfoQuery(GwtDataChannelInfoQuery query, PagingLoadConfig pagingLoadConfig) {
            ChannelInfoQueryImpl channelInfoQuery = new ChannelInfoQueryImpl(GwtKapuaCommonsModelConverter.convertKapuaId(query.getScopeId()));
            channelInfoQuery.setOffset(pagingLoadConfig.getOffset());
            channelInfoQuery.setLimit(pagingLoadConfig.getLimit());
            return channelInfoQuery;
        }
}
