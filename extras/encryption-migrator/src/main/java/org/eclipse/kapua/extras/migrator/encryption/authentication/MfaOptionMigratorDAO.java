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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionCreator;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionListResult;

/**
 * MfaOption DAO
 *
 * @since 2.0.0
 */
public class MfaOptionMigratorDAO {

    private MfaOptionMigratorDAO() {
    }

    public static MfaOption update(EntityManager em, MfaOption mfaOption) throws KapuaException {
        MfaOptionMigrator mfaOptionImpl = (MfaOptionMigrator) mfaOption;

        mfaOptionImpl.setMfaSecretKey(mfaOptionImpl.getMfaSecretKey());

        return ServiceDAO.update(em, MfaOptionMigrator.class, mfaOptionImpl);
    }

    public static MfaOptionListResult query(EntityManager em, KapuaQuery mfaOptionQuery) throws KapuaException {
        return ServiceDAO.query(em, MfaOption.class, MfaOptionMigrator.class, new MfaOptionMigratorListResultImpl(), mfaOptionQuery);
    }

    public static long count(EntityManager em, KapuaQuery mfaOptionQuery) throws KapuaException {
        return ServiceDAO.count(em, MfaOption.class, MfaOptionMigrator.class, mfaOptionQuery);
    }

    //
    // Unsupported methods
    //

    public static MfaOption create(EntityManager em, MfaOptionCreator mfaOptionCreator) {
        throw new UnsupportedOperationException();
    }

    public static MfaOption find(EntityManager em, KapuaId scopeId, KapuaId mfaOptionId) {
        throw new UnsupportedOperationException();
    }

    public static MfaOption findByName(EntityManager em, String name) {
        throw new UnsupportedOperationException();
    }

    public static MfaOption delete(EntityManager em, KapuaId scopeId, KapuaId mfaOptionId) throws KapuaEntityNotFoundException {
        throw new UnsupportedOperationException();
    }
}
