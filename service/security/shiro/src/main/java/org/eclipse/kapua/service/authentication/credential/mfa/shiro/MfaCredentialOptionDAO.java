/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.credential.mfa.shiro;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaCredentialOption;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaCredentialOptionCreator;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaCredentialOptionListResult;
import org.eclipse.kapua.service.authentication.shiro.utils.AuthenticationUtils;

/**
 * {@link MfaCredentialOption} {@link ServiceDAO}
 */
public class MfaCredentialOptionDAO extends ServiceDAO {

    /**
     * Creates and return new credential
     *
     * @param em
     * @param mfaCredentialCreator
     * @return
     * @throws KapuaException
     */
    public static MfaCredentialOption create(EntityManager em, MfaCredentialOptionCreator mfaCredentialCreator) throws KapuaException {

        //
        // Crypt MfaCredentialOption key
        String cryptedSecretKey = AuthenticationUtils.encryptAes(mfaCredentialCreator.getMfaCredentialKey());

        //
        // Create MfaCredentialOption
        MfaCredentialOptionImpl credentialImpl = new MfaCredentialOptionImpl(mfaCredentialCreator.getScopeId(), mfaCredentialCreator.getUserId(),
                cryptedSecretKey);
        //
        // Do create
        return ServiceDAO.create(em, credentialImpl);
    }

    /**
     * Update the provided MfaCredentialOption
     *
     * @param em
     * @param mfaCredentialOption
     * @return
     * @throws KapuaException
     */
    public static MfaCredentialOption update(EntityManager em, MfaCredentialOption mfaCredentialOption)
            throws KapuaException {
        //
        // Update credential
        MfaCredentialOptionImpl mfaCredentialOptionImpl = (MfaCredentialOptionImpl) mfaCredentialOption;

        return ServiceDAO.update(em, MfaCredentialOptionImpl.class, mfaCredentialOptionImpl);
    }

    /**
     * Find the MfaCredentialOption by mfaCredentialOption identifier
     *
     * @param em
     * @param scopeId
     * @param mfaCredentialOptionId
     * @return
     */
    public static MfaCredentialOption find(EntityManager em, KapuaId scopeId, KapuaId mfaCredentialOptionId) {
        return ServiceDAO.find(em, MfaCredentialOptionImpl.class, scopeId, mfaCredentialOptionId);
    }

    /**
     * Return the MfaCredentialOption list matching the provided query
     *
     * @param em
     * @param mfaCredentialOptionQuery
     * @return
     * @throws KapuaException
     */
    public static MfaCredentialOptionListResult query(EntityManager em, KapuaQuery mfaCredentialOptionQuery) throws KapuaException {
        return ServiceDAO.query(em, MfaCredentialOption.class, MfaCredentialOptionImpl.class, new MfaCredentialOptionListResultImpl(), mfaCredentialOptionQuery);
    }

    /**
     * Return the MfaCredentialOption count matching the provided query
     *
     * @param em
     * @param mfaCredentialOptionQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery mfaCredentialOptionQuery) throws KapuaException {
        return ServiceDAO.count(em, MfaCredentialOption.class, MfaCredentialOptionImpl.class, mfaCredentialOptionQuery);
    }

    /**
     * Delete the MfaCredentialOption by MfaCredentialOption identifier
     *
     * @param em
     * @param scopeId
     * @param mfaCredentialOptionId
     * @return deleted entity
     * @throws KapuaEntityNotFoundException If {@link MfaCredentialOption} is now found.
     */
    public static MfaCredentialOption delete(EntityManager em, KapuaId scopeId, KapuaId mfaCredentialOptionId) throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, MfaCredentialOptionImpl.class, scopeId, mfaCredentialOptionId);
    }
}
