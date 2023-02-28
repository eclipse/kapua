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

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.storage.KapuaEntityRepositoryBareToTransactionalWrapper;
import org.eclipse.kapua.storage.KapuaEntityTransactedRepository;
import org.eclipse.kapua.storage.TxManager;

import java.util.function.Supplier;

public class KapuaEntityJpaTransactedRepository<E extends KapuaEntity, C extends E, L extends KapuaListResult<E>>
        extends KapuaEntityRepositoryBareToTransactionalWrapper<E, L>
        implements KapuaEntityTransactedRepository<E, L> {

    public KapuaEntityJpaTransactedRepository(TxManager txManager,
                                              Class<C> concreteClass,
                                              Supplier<L> listSupplier) {
        super(txManager, new KapuaEntityJpaBareRepository<E, C, L>(concreteClass, listSupplier));
    }
}
