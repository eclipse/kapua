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
package org.eclipse.kapua.storage;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;

public class KapuaEntityRepositoryBareToTransactionalWrapper<E extends KapuaEntity, L extends KapuaListResult<E>>
        implements KapuaEntityTransactedRepository<E, L> {

    protected final TxManager txManager;
    protected final KapuaEntityBareRepository<E, L> bareRepository;

    public KapuaEntityRepositoryBareToTransactionalWrapper(TxManager txManager, KapuaEntityBareRepository<E, L> bareRepository) {
        this.txManager = txManager;
        this.bareRepository = bareRepository;
    }

    @Override
    public E create(E entity) throws KapuaException {
        return txManager.executeWithResult(txHolder -> bareRepository.create(txHolder, entity));
    }

    @Override
    public E find(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        return txManager.executeWithResult(txHolder -> bareRepository.find(txHolder, scopeId, entityId));
    }

    @Override
    public L query(KapuaQuery kapuaQuery) throws KapuaException {
        return txManager.executeWithResult(txHolder -> bareRepository.query(txHolder, kapuaQuery));
    }

    @Override
    public long count(KapuaQuery kapuaQuery) throws KapuaException {
        return txManager.executeWithResult(txHolder -> bareRepository.count(txHolder, kapuaQuery));
    }

    @Override
    public E delete(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        return txManager.executeWithResult(txHolder -> bareRepository.delete(txHolder, scopeId, entityId));
    }
}
