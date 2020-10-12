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
import org.eclipse.kapua.service.datastore.ClientInfoFactory;
import org.eclipse.kapua.service.datastore.internal.model.ClientInfoImpl;
import org.eclipse.kapua.service.datastore.internal.model.ClientInfoListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ClientInfoQueryImpl;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;

/**
 * {@link ClientInfoFactory} implementation.
 *
 * @since 1.3.0
 */
@KapuaProvider
public class ClientInfoFactoryImpl implements ClientInfoFactory {

    @Override
    public ClientInfo newStorable() {
        return new ClientInfoImpl();
    }

    @Override
    public ClientInfoListResult newListResult() {
        return new ClientInfoListResultImpl();
    }

    @Override
    public ClientInfoQuery newQuery(KapuaId scopeId) {
        return new ClientInfoQueryImpl(scopeId);
    }
}
