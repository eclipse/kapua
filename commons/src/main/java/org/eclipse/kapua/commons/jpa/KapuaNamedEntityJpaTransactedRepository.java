/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.jpa;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.storage.KapuaNamedEntityBareRepository;
import org.eclipse.kapua.storage.KapuaNamedEntityRepositoryBareToTransactionalWrapper;
import org.eclipse.kapua.storage.KapuaNamedEntityTransactedRepository;
import org.eclipse.kapua.storage.TxManager;

import java.util.function.Supplier;

public class KapuaNamedEntityJpaTransactedRepository<E extends KapuaNamedEntity, C extends E, L extends KapuaListResult<E>>
        extends KapuaNamedEntityRepositoryBareToTransactionalWrapper<E, L>
        implements KapuaNamedEntityTransactedRepository<E, L> {
    public KapuaNamedEntityJpaTransactedRepository(TxManager txManager,
                                                   Class<C> concreteClass,
                                                   Supplier<L> listSupplier) {
        super(txManager, new KapuaNamedEntityJpaBareRepository<>(concreteClass, listSupplier));
    }

    @Override
    public E findByName(String value) throws KapuaException {
        return txManager.executeWithResult(tx -> ((KapuaNamedEntityBareRepository<E, L>) this.bareRepository).findByName(tx, value));
    }

    @Override
    public E findByName(KapuaId scopeId, String value) throws KapuaException {
        return txManager.executeWithResult(tx -> ((KapuaNamedEntityBareRepository<E, L>) this.bareRepository).findByName(tx, scopeId, value));
    }
}
