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
package org.eclipse.kapua.service.datastore.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.ClientInfoRegistryService;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;

/*******************************************************************************
 * This proxy class is only used to acces sthe otherwise package-restricted
 * methods of the Client Info Registry service.
 *
 * At the moment the only method that is required is delete().
 *
 *******************************************************************************/

public class ClientInfoRegistryServiceProxy {
    private static final ClientInfoRegistryService CLIENT_INFO_REGISTRY_SERVICE =
            KapuaLocator.getInstance().getService(ClientInfoRegistryService.class);

    public void delete(KapuaId scopeId, StorableId id) throws KapuaException {
        ((ClientInfoRegistryServiceImpl) CLIENT_INFO_REGISTRY_SERVICE).delete(scopeId, id);
    }

    public void delete(ClientInfoQuery query) throws KapuaException {
        ((ClientInfoRegistryServiceImpl) CLIENT_INFO_REGISTRY_SERVICE).delete(query);
    }
}
