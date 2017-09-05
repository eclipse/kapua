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
import org.eclipse.kapua.service.datastore.MetricInfoRegistryService;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;

/*******************************************************************************
 * This proxy class is only used to acces sthe otherwise package-restricted
 * methods of the Metric Info Registry service.
 *
 * At the moment the only method that is required is delete().
 *
 *******************************************************************************/

public class MetricInfoRegistryServiceProxy {
    private static final MetricInfoRegistryService METRIC_INFO_REGISTRY_SERVICE =
            KapuaLocator.getInstance().getService(MetricInfoRegistryService.class);

    public void delete(KapuaId scopeId, StorableId id) throws KapuaException {
        ((MetricInfoRegistryServiceImpl) METRIC_INFO_REGISTRY_SERVICE).delete(scopeId, id);
    }

    public void delete(MetricInfoQuery query) throws KapuaException {
        ((MetricInfoRegistryServiceImpl) METRIC_INFO_REGISTRY_SERVICE).delete(query);
    }
}
