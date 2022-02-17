/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.ChannelInfoRegistryService;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;
import org.eclipse.kapua.service.storable.model.id.StorableId;

/*******************************************************************************
 * This proxy class is only used to acces sthe otherwise package-restricted
 * methods of the Channel Info Registry service.
 *
 * At the moment the only method that is required is delete().
 *
 *******************************************************************************/

public class ChannelInfoRegistryServiceProxy {
    private static final ChannelInfoRegistryService CHANNEL_INFO_REGISTRY_SERVICE =
            KapuaLocator.getInstance().getService(ChannelInfoRegistryService.class);

    public void delete(KapuaId scopeId, StorableId id) throws KapuaException {
        ((ChannelInfoRegistryServiceImpl) CHANNEL_INFO_REGISTRY_SERVICE).delete(scopeId, id);
    }

    public void delete(ChannelInfoQuery query) throws KapuaException {
        ((ChannelInfoRegistryServiceImpl) CHANNEL_INFO_REGISTRY_SERVICE).delete(query);
    }
}
