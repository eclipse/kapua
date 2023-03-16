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
package org.eclipse.kapua.service.authentication.credential.shiro;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityJpaRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.QueryPredicate;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialAttributes;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialQuery;
import org.eclipse.kapua.service.authentication.credential.CredentialRepository;
import org.eclipse.kapua.storage.TxContext;

public class CredentialImplJpaRepository
        extends KapuaUpdatableEntityJpaRepository<Credential, CredentialImpl, CredentialListResult>
        implements CredentialRepository {

    public CredentialImplJpaRepository() {
        super(CredentialImpl.class, () -> new CredentialListResultImpl());
    }

    @Override
    public CredentialListResult findByUserId(TxContext txContext, KapuaId scopeId, KapuaId userId) throws KapuaException {
        // Build query
        CredentialQuery query = new CredentialQueryImpl(scopeId);
        QueryPredicate predicate = query.attributePredicate(CredentialAttributes.USER_ID, userId);
        query.setPredicate(predicate);
        // Query and return result
        return this.query(txContext, query);
    }

    //Overwritten just to change the exception type
    @Override
    public Credential delete(TxContext tx, KapuaId scopeId, KapuaId credentialId) throws KapuaException {
        final Credential toBeDeleted = this.find(tx, scopeId, credentialId);
        if (toBeDeleted == null) {
            throw new KapuaEntityNotFoundException(Credential.TYPE, credentialId);
        }
        return this.delete(tx, toBeDeleted);
    }
}
