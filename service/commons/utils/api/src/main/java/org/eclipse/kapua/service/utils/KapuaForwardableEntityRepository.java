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
package org.eclipse.kapua.service.utils;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaForwardableEntity;
import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.query.KapuaForwardableEntityQuery;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.storage.KapuaEntityRepository;
import org.eclipse.kapua.storage.KapuaNamedEntityRepository;
import org.eclipse.kapua.storage.KapuaUpdatableEntityRepository;
import org.eclipse.kapua.storage.TxContext;

/**
 * This contract builds upon {@link KapuaUpdatableEntityRepository} (and in turn {@link KapuaEntityRepository},
 * adding functionalities specific to Kapua Entities that are implement the contract {@link KapuaNamedEntity} (as in: have a name and description fields)
 *
 * @param <E> The specific subclass of {@link KapuaEntity} handled by this repository
 * @param <L> The specific subclass of {@link KapuaListResult}&lt;E&gt; meant to hold list results for the kapua entity handled by this repo
 * @since 2.0.0
 */
public interface KapuaForwardableEntityRepository<E extends KapuaForwardableEntity, L extends KapuaListResult<E>>
        extends KapuaNamedEntityRepository<E, L> {
    L query(TxContext txContext, KapuaForwardableEntityQuery kapuaQuery) throws KapuaException;

    long count(TxContext txContext, KapuaForwardableEntityQuery kapuaQuery) throws KapuaException;

}
