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
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.extras.migrator.encryption.MigratorEntityManagerFactory;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionCreator;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionListResult;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionService;

import javax.inject.Singleton;

/**
 * {@link MfaOptionService} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class MfaOptionMigratorServiceImpl extends AbstractKapuaService implements MfaOptionService {

    public MfaOptionMigratorServiceImpl() {
        super(MigratorEntityManagerFactory.getInstance(), null);
    }

    @Override
    public MfaOption update(MfaOption mfaOption) throws KapuaException {
        return entityManagerSession.doTransactedAction((em) -> MfaOptionMigratorDAO.update(em, mfaOption));
    }

    @Override
    public MfaOptionListResult query(KapuaQuery query) throws KapuaException {
        return entityManagerSession.doAction(em -> MfaOptionMigratorDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        return entityManagerSession.doAction(em -> MfaOptionMigratorDAO.count(em, query));
    }

    //
    // Unsupported methods
    //

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
    public String enableTrust(MfaOption mfaOption) {
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
}
