/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityJpaRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.QueryPredicate;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCode;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeAttributes;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeListResult;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeQuery;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeRepository;
import org.eclipse.kapua.storage.TxContext;

public class ScratchCodeImplJpaRepository
        extends KapuaUpdatableEntityJpaRepository<ScratchCode, ScratchCodeImpl, ScratchCodeListResult>
        implements ScratchCodeRepository {

    public ScratchCodeImplJpaRepository() {
        super(ScratchCodeImpl.class, () -> new ScratchCodeListResultImpl());
    }

    @Override
    public ScratchCode delete(TxContext tx, KapuaId scopeId, KapuaId scratchCodeId) throws KapuaException {
        final ScratchCode scratchCode = this.find(tx, scopeId, scratchCodeId);
        if (scratchCode == null) {
            throw new KapuaEntityNotFoundException(ScratchCode.TYPE, scratchCodeId);
        }
        return this.delete(tx, scratchCode);

    }

    @Override
    public ScratchCodeListResult findByMfaOptionId(TxContext tx, KapuaId scopeId, KapuaId mfaOptionId) throws KapuaException {

        //
        // Build query
        ScratchCodeQuery query = new ScratchCodeQueryImpl(scopeId);
        QueryPredicate predicate = query.attributePredicate(ScratchCodeAttributes.MFA_OPTION_ID, mfaOptionId);
        query.setPredicate(predicate);

        //
        // Query and return result
        return this.query(tx, query);
    }
}
