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

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.storage.KapuaUpdatableEntityRepositoryBareToTransactionalWrapper;
import org.eclipse.kapua.storage.KapuaUpdatableEntityTransactedRepository;
import org.eclipse.kapua.storage.TxManager;

import java.util.function.Supplier;

public class KapuaUpdatableEntityJpaTransactedRepository<E extends KapuaUpdatableEntity, C extends E, L extends KapuaListResult<E>>
        extends KapuaUpdatableEntityRepositoryBareToTransactionalWrapper<E, L>
        implements KapuaUpdatableEntityTransactedRepository<E, L> {
    public KapuaUpdatableEntityJpaTransactedRepository(TxManager txManager,
                                                       Class<C> concreteClass,
                                                       Supplier<L> listSupplier) {
        super(txManager, new KapuaUpdatableEntityJpaBareRepository<>(concreteClass, listSupplier));
    }
}
