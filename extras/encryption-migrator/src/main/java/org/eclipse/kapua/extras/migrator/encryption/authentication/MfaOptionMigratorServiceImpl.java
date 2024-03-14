/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.extras.migrator.encryption.authentication;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionCreator;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionListResult;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionRepository;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionService;
import org.eclipse.kapua.storage.TxManager;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * {@link MfaOptionService} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class MfaOptionMigratorServiceImpl implements MfaOptionService {

    private final TxManager txManager;
    private final MfaOptionRepository repository;

    @Inject
    public MfaOptionMigratorServiceImpl(TxManager txManager, MfaOptionRepository repository) {
        this.txManager = txManager;
        this.repository = repository;
    }

    @Override
    public MfaOption update(MfaOption mfaOption) throws KapuaException {
        return txManager.execute(tx -> repository.update(tx, mfaOption));
    }

    @Override
    public MfaOptionListResult query(KapuaQuery query) throws KapuaException {
        return txManager.execute(tx -> repository.query(tx, query));
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        return txManager.execute(tx -> repository.count(tx, query));
    }
    // Unsupported methods

    @Override
    public MfaOption create(MfaOptionCreator mfaOptionCreator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MfaOption find(KapuaId scopeId, KapuaId mfaOptionId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId mfaOptionId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MfaOption findByUserId(KapuaId scopeId, KapuaId userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String enableTrust(KapuaId scopeId, KapuaId mfaOptionId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void disableTrust(KapuaId scopeId, KapuaId mfaOptionId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void disableTrustByUserId(KapuaId scopeId, KapuaId userId) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteByUserId(KapuaId scopeId, KapuaId userId) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean validateMfaCredentials(KapuaId scopeId, KapuaId userId, String tokenAuthenticationCode, String tokenTrustKey) throws KapuaException {
        throw new UnsupportedOperationException();
    }
}
