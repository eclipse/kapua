/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
