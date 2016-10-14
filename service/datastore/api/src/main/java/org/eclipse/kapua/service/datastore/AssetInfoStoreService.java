/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.datastore;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;
import org.eclipse.kapua.service.datastore.model.AssetInfo;
import org.eclipse.kapua.service.datastore.model.AssetInfoListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.AssetInfoQuery;

public interface AssetInfoStoreService extends KapuaService,
                                       KapuaConfigurableService
{
    // public StorableId store(KapuaId scopeId, AssetInfoCreator creator)
    // throws KapuaException;
    //
    // public StorableId update(KapuaId scopeId, AssetInfo assetInfo)
    // throws KapuaException;
    //
    public void delete(KapuaId scopeId, StorableId id)
        throws KapuaException;

    public AssetInfo find(KapuaId scopeId, StorableId id)
        throws KapuaException;

    public AssetInfoListResult query(KapuaId scopeId, AssetInfoQuery query)
        throws KapuaException;

    public long count(KapuaId scopeId, AssetInfoQuery query)
        throws KapuaException;

    public void delete(KapuaId scopeId, AssetInfoQuery query)
        throws KapuaException;
}
