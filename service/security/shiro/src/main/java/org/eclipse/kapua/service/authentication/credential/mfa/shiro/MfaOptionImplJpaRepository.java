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
import org.eclipse.kapua.commons.jpa.JpaAwareTxContext;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityJpaRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionListResult;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionRepository;
import org.eclipse.kapua.storage.TxContext;

import javax.persistence.EntityManager;
import java.util.Optional;

public class MfaOptionImplJpaRepository
        extends KapuaUpdatableEntityJpaRepository<MfaOption, MfaOptionImpl, MfaOptionListResult>
        implements MfaOptionRepository {
    public MfaOptionImplJpaRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        super(MfaOptionImpl.class, () -> new MfaOptionListResultImpl(), jpaRepoConfig);
    }

    //Overwritten for the sole purpose of changing the exception thrown
    @Override
    public MfaOption update(TxContext txContext, MfaOption mfaOption) throws KapuaException {
        final javax.persistence.EntityManager em = JpaAwareTxContext.extractEntityManager(txContext);
        return this.doFind(em, mfaOption.getScopeId(), mfaOption.getId())
                // Passing attributes??
                .map(currentEntity -> doUpdate(em, currentEntity, mfaOption))
                .orElseThrow(() -> new KapuaEntityNotFoundException(MfaOption.TYPE, mfaOption.getId()));
    }

    @Override
    public Optional<MfaOption> findByUserId(TxContext tx, KapuaId scopeId, KapuaId userId) throws KapuaException {
        return doFindByField(tx, scopeId, MfaOptionImpl_.USER_ID, userId);
    }

    @Override
    public MfaOption delete(TxContext txContext, KapuaId scopeId, KapuaId mfaOptionId) throws KapuaException {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(txContext);
        return this.doFind(em, scopeId, mfaOptionId)
                .map(toDelete -> doDelete(em, toDelete))
                .orElseThrow(() -> new KapuaEntityNotFoundException(MfaOption.TYPE, mfaOptionId));
    }
}
