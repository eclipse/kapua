/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.model;

import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;
import org.eclipse.kapua.service.elasticsearch.client.model.ResultList;
import org.eclipse.kapua.service.storable.model.AbstractStorableListResult;

/**
 * Metric information query result list implementation
 *
 * @since 1.0.0
 */
public class MetricInfoListResultImpl extends AbstractStorableListResult<MetricInfo> implements MetricInfoListResult {

    private static final long serialVersionUID = 9057086672566426909L;

    /**
     * Construct a metric info result list
     *
     * @since 1.0.0
     */
    public MetricInfoListResultImpl() {
        super();
    }

    /**
     * Construct the metric info result list from the provided list
     *
     * @param resultList
     * @since 1.0.0
     */
    public MetricInfoListResultImpl(ResultList<MetricInfo> resultList) {
        super(resultList.getResult(), resultList.getTotalCount());
    }

}
