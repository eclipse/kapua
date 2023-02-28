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
import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;

public class KapuaNamedEntityRepositoryBareToTransactionalWrapper<E extends KapuaNamedEntity, L extends KapuaListResult<E>>
        extends KapuaUpdatableEntityRepositoryBareToTransactionalWrapper<E, L>
        implements KapuaNamedEntityTransactedRepository<E, L> {
    protected final KapuaNamedEntityBareRepository<E, L> namedEntityBareRepository;

    public KapuaNamedEntityRepositoryBareToTransactionalWrapper(TxManager txManager, KapuaNamedEntityBareRepository<E, L> bareRepository) {
        super(txManager, bareRepository);
        this.namedEntityBareRepository = bareRepository;
    }


    @Override
    public E findByName(String value) throws KapuaException {
        return txManager.executeWithResult(txHolder -> namedEntityBareRepository.findByName(txHolder, value));

    }

    @Override
    public E findByName(KapuaId scopeId, String value) throws KapuaException {
        return txManager.executeWithResult(txHolder -> namedEntityBareRepository.findByName(txHolder, scopeId, value));
    }
}
