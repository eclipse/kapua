/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.datastore.internal.mediator;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.schema.Metadata;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.storable.exception.MappingException;

/**
 * Metric information registry mediator definition
 *
 * @since 1.0
 */
public interface MetricInfoRegistryMediator {

    /**
     * Get the metric info metadata
     *
     * @param scopeId
     * @param indexedOn
     * @return
     * @throws ClientException
     */
    Metadata getMetadata(KapuaId scopeId, long indexedOn)
            throws ClientException, MappingException;

    /**
     * On after metric info delete event handler
     *
     * @param scopeId
     * @param metricInfo
     * @throws ClientException
     */
    void onAfterMetricInfoDelete(KapuaId scopeId, MetricInfo metricInfo) throws ClientException;
}
