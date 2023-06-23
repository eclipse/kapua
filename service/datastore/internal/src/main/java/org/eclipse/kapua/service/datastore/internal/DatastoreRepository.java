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

import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.elasticsearch.client.model.ResultList;
import org.eclipse.kapua.service.storable.model.Storable;
import org.eclipse.kapua.service.storable.model.query.StorableQuery;

public interface DatastoreRepository<T extends Storable, Q extends StorableQuery> {
    ResultList<T> query(String dataIndexName, Q query) throws ClientException;

    long count(String dataIndexName, Q query) throws ClientException;

    void delete(String indexName, String id) throws ClientException;

    void delete(String indexName, Q query) throws ClientException;
}
