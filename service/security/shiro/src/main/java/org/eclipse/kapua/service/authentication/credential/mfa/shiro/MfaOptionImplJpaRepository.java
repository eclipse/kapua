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
package org.eclipse.kapua.service.authentication.credential.mfa.shiro;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityJpaRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.QueryPredicate;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionAttributes;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionListResult;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionQuery;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionRepository;
import org.eclipse.kapua.storage.TxContext;

import java.util.Optional;

public class MfaOptionImplJpaRepository
        extends KapuaUpdatableEntityJpaRepository<MfaOption, MfaOptionImpl, MfaOptionListResult>
        implements MfaOptionRepository {
    public MfaOptionImplJpaRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        super(MfaOptionImpl.class, () -> new MfaOptionListResultImpl(), jpaRepoConfig);
    }


    //Overwritten for the sole purpose of changing the exception thrown
    @Override
    public MfaOption update(TxContext tx, MfaOption mfaOption) throws KapuaException {
        final MfaOption currentMfaOption = this.find(tx, mfaOption.getScopeId(), mfaOption.getId())
                .orElseThrow(() -> new KapuaEntityNotFoundException(MfaOption.TYPE, mfaOption.getId()));

        // Passing attributes??
        return this.update(tx, currentMfaOption, mfaOption);
    }

    @Override
    public MfaOption findByUserId(TxContext tx, KapuaId scopeId, KapuaId userId) throws KapuaException {
        // Build query
        MfaOptionQuery query = new MfaOptionQueryImpl(scopeId);
        QueryPredicate predicate = query.attributePredicate(MfaOptionAttributes.USER_ID, userId);
        query.setPredicate(predicate);
        // Query and return result
        MfaOptionListResult result = this.query(tx, query);

        return result.getFirstItem();
    }

    @Override
    public MfaOption delete(TxContext txContext, KapuaId scopeId, KapuaId mfaOptionId) throws KapuaException {
        final Optional<MfaOption> toDelete = this.find(txContext, scopeId, mfaOptionId);
        if (!toDelete.isPresent()) {
            throw new KapuaEntityNotFoundException(MfaOption.TYPE, mfaOptionId);
        }
        return this.delete(txContext, toDelete.get());
    }
}
