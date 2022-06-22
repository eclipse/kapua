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
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionCreator;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionListResult;

/**
 * {@link MfaOption} {@link ServiceDAO}
 */
public class MfaOptionDAO extends ServiceDAO {

    /**
     * Creates and return new {@link MfaOption}
     *
     * @param em
     * @param mfaOptionCreator
     * @return
     * @throws KapuaException
     */
    public static MfaOption create(EntityManager em, MfaOptionCreator mfaOptionCreator) throws KapuaException {

        //
        // Create MfaOption
        MfaOptionImpl mfaOptionImpl = new MfaOptionImpl(mfaOptionCreator.getScopeId());
        mfaOptionImpl.setUserId(mfaOptionCreator.getUserId());
        mfaOptionImpl.setMfaSecretKey(mfaOptionCreator.getMfaSecretKey());

        //
        // Do create
        return ServiceDAO.create(em, mfaOptionImpl);
    }

    /**
     * Update the provided {@link MfaOption}
     *
     * @param em
     * @param mfaOption
     * @return
     * @throws KapuaException
     */
    public static MfaOption update(EntityManager em, MfaOption mfaOption)
            throws KapuaException {
        //
        // Update
        MfaOptionImpl mfaOptionImpl = (MfaOptionImpl) mfaOption;

        return ServiceDAO.update(em, MfaOptionImpl.class, mfaOptionImpl);
    }

    /**
     * Find the {@link MfaOption} by its {@link KapuaId}
     *
     * @param em
     * @param scopeId
     * @param mfaOptionId
     * @return
     */
    public static MfaOption find(EntityManager em, KapuaId scopeId, KapuaId mfaOptionId) {
        return ServiceDAO.find(em, MfaOptionImpl.class, scopeId, mfaOptionId);
    }

    /**
     * Return the {@link MfaOption} list matching the provided query
     *
     * @param em
     * @param mfaOptionQuery
     * @return
     * @throws KapuaException
     */
    public static MfaOptionListResult query(EntityManager em, KapuaQuery mfaOptionQuery) throws KapuaException {
        return ServiceDAO.query(em, MfaOption.class, MfaOptionImpl.class, new MfaOptionListResultImpl(), mfaOptionQuery);
    }

    /**
     * Return the {@link MfaOption} count matching the provided query
     *
     * @param em
     * @param mfaOptionQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery mfaOptionQuery) throws KapuaException {
        return ServiceDAO.count(em, MfaOption.class, MfaOptionImpl.class, mfaOptionQuery);
    }

    /**
     * Delete the {@link MfaOption} by its {@link KapuaId}
     *
     * @param em
     * @param scopeId
     * @param mfaOptionId
     * @return deleted entity
     * @throws KapuaEntityNotFoundException If {@link MfaOption} is now found.
     */
    public static MfaOption delete(EntityManager em, KapuaId scopeId, KapuaId mfaOptionId) throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, MfaOptionImpl.class, scopeId, mfaOptionId);
    }
}
