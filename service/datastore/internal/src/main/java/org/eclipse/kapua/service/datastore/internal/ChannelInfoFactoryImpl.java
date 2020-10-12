/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.ChannelInfoFactory;
import org.eclipse.kapua.service.datastore.internal.model.ChannelInfoImpl;
import org.eclipse.kapua.service.datastore.internal.model.ChannelInfoListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelInfoQueryImpl;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.ChannelInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;

/**
 * {@link ChannelInfoFactory} implementation.
 *
 * @since 1.3.0
 */
@KapuaProvider
public class ChannelInfoFactoryImpl implements ChannelInfoFactory {

    @Override
    public ChannelInfo newStorable() {
        return new ChannelInfoImpl();
    }

    @Override
    public ChannelInfoListResult newListResult() {
        return new ChannelInfoListResultImpl();
    }

    @Override
    public ChannelInfoQuery newQuery(KapuaId scopeId) {
        return new ChannelInfoQueryImpl(scopeId);
    }
}
