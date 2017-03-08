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
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsSchema.Metadata;
import org.eclipse.kapua.service.datastore.model.MetricInfo;

/**
 * Metric information registry mediator definition
 * 
 * @since 1.0
 *
 */
public interface MetricInfoRegistryMediator
{

    /**
     * Get the metric info metadata
     * 
     * @param scopeId
     * @param indexedOn
     * @return
     * @throws EsDocumentBuilderException
     * @throws EsClientUnavailableException
     */
    public Metadata getMetadata(KapuaId scopeId, long indexedOn)
        throws EsDocumentBuilderException, EsClientUnavailableException;

    /**
     * On after metric info delete event handler
     * 
     * @param scopeId
     * @param metricInfo
     */
    public void onAfterMetricInfoDelete(KapuaId scopeId, MetricInfo metricInfo);
}
