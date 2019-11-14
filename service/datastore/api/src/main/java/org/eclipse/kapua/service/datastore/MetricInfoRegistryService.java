/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;

/**
 * Metric information registry service definition.<br>
 * Service dedicated for storing and reading named device metrics. Metric is an arbitrary named value. We usually
 * keep only the most recent value of the metric.
 *
 * @since 1.0.0
 */
public interface MetricInfoRegistryService extends KapuaService {

    /**
     * Find metric information by identifier
     *
     * @param scopeId
     * @param id
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    MetricInfo find(KapuaId scopeId, StorableId id) throws KapuaException;

    /**
     * Query for metrics informations objects matching the given query
     *
     * @param query
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    MetricInfoListResult query(MetricInfoQuery query) throws KapuaException;

    /**
     * Get the metrics informations count matching the given query
     *
     * @param query
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    long count(MetricInfoQuery query) throws KapuaException;

}
