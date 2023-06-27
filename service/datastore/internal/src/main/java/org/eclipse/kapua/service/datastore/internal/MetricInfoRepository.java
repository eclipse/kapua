/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.elasticsearch.client.model.BulkUpdateResponse;

import java.util.List;

public interface MetricInfoRepository extends DatastoreRepository<MetricInfo, MetricInfoQuery> {
    String upsert(String metricInfoId, MetricInfo metricInfo) throws ClientException;

    BulkUpdateResponse upsert(List<MetricInfo> metricInfos) throws ClientException;
}
