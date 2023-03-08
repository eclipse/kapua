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
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.service.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.QueryPredicate;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.storage.TxContext;

public interface DuplicateNameChecker<E extends KapuaNamedEntity> extends KapuaService {
    long countOtherEntitiesWithName(TxContext tx, KapuaId scopeId, KapuaId excludedId, String nameQueryPredicate, QueryPredicate... additionalPredicates) throws KapuaException;

    long countOtherEntitiesWithName(TxContext tx, KapuaId scopeId, String name, QueryPredicate... additionalPredicates) throws KapuaException;

    long countOtherEntitiesWithName(TxContext tx, String name, QueryPredicate... additionalPredicates) throws KapuaException;
}
