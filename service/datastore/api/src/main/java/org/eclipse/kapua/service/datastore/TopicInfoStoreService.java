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
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.TopicInfo;
import org.eclipse.kapua.service.datastore.model.TopicInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.TopicInfoQuery;

public interface TopicInfoStoreService extends KapuaService,
                                   KapuaConfigurableService
{
//    public StorableId store(KapuaId scopeId, TopicInfoCreator creator)
//        throws KapuaException;
//
//    public StorableId update(KapuaId scopeId, TopicInfo creator)
//            throws KapuaException;
//
    public void delete(KapuaId scopeId, StorableId id)
        throws KapuaException;

    public TopicInfo find(KapuaId scopeId, StorableId id)
        throws KapuaException;

    public TopicInfoListResult query(KapuaId scopeId, TopicInfoQuery query)
        throws KapuaException;

    public long count(KapuaId scopeId, TopicInfoQuery query)
        throws KapuaException;

    public void delete(KapuaId scopeId, TopicInfoQuery query)
        throws KapuaException;
}
