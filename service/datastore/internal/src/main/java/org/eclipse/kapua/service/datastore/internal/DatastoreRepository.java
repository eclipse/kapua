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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.elasticsearch.client.model.ResultList;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.eclipse.kapua.service.storable.model.Storable;
import org.eclipse.kapua.service.storable.model.query.StorableQuery;

public interface DatastoreRepository<T extends Storable, Q extends StorableQuery> {
    ResultList<T> query(Q query) throws ClientException;

    long count(Q query) throws ClientException;

    void delete(KapuaId scopeId, String id) throws ClientException;

    void delete(Q query) throws ClientException;

    void upsertIndex(KapuaId scopeId) throws ClientException, MappingException;
}
