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
package org.eclipse.kapua.service.utils.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.jpa.KapuaNamedEntityJpaRepository;
import org.eclipse.kapua.model.KapuaForwardableEntity;
import org.eclipse.kapua.model.query.KapuaForwardableEntityQuery;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.utils.KapuaEntityQueryUtil;
import org.eclipse.kapua.service.utils.KapuaForwardableEntityRepository;
import org.eclipse.kapua.storage.TxContext;

import java.util.function.Supplier;

public class KapuaForwardableEntityJpaRepository<E extends KapuaForwardableEntity, C extends E, L extends KapuaListResult<E>>
        extends KapuaNamedEntityJpaRepository<E, C, L>
        implements KapuaForwardableEntityRepository<E, L> {
    private final KapuaEntityQueryUtil kapuaEntityQueryUtil;

    public KapuaForwardableEntityJpaRepository(
            Class<C> concreteClass,
            String entityName,
            Supplier<L> listSupplier,
            KapuaJpaRepositoryConfiguration jpaRepoConfig,
            KapuaEntityQueryUtil kapuaEntityQueryUtil) {
        super(concreteClass, entityName, listSupplier, jpaRepoConfig);
        this.kapuaEntityQueryUtil = kapuaEntityQueryUtil;
    }

    @Override
    public L query(TxContext txContext, KapuaForwardableEntityQuery kapuaQuery) throws KapuaException {
        return doQuery(txContext, kapuaQuery);
    }

    @Override
    public L query(TxContext txContext, KapuaQuery listQuery) throws KapuaException {
        // Transform the query for the includeInherited option
        return (listQuery instanceof KapuaForwardableEntityQuery)
                ? this.doQuery(txContext, (KapuaForwardableEntityQuery) listQuery) : super.query(txContext, listQuery);
    }

    private L doQuery(TxContext txContext, KapuaForwardableEntityQuery kapuaQuery) throws KapuaException {
        final KapuaQuery query = kapuaEntityQueryUtil.transformInheritedQuery(kapuaQuery);
        return super.query(txContext, query);
    }

    @Override
    public long count(TxContext txContext, KapuaForwardableEntityQuery kapuaQuery) throws KapuaException {
        return doCount(txContext, kapuaQuery);
    }

    @Override
    public long count(TxContext txContext, KapuaQuery countQuery) throws KapuaException {
        // Transform the query for the includeInherited option
        return (countQuery instanceof KapuaForwardableEntityQuery)
                ? this.count(txContext, (KapuaForwardableEntityQuery) countQuery) : super.count(txContext, countQuery);
    }

    private long doCount(TxContext txContext, KapuaForwardableEntityQuery kapuaQuery) throws KapuaException {
        final KapuaQuery query = kapuaEntityQueryUtil.transformInheritedQuery(kapuaQuery);
        return super.count(txContext, query);
    }
}
