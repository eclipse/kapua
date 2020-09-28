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
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCode;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeCreator;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeListResult;
import org.eclipse.kapua.service.authentication.shiro.utils.AuthenticationUtils;
import org.eclipse.kapua.service.authentication.shiro.utils.CryptAlgorithm;

/**
 * {@link ScratchCode} {@link ServiceDAO}
 */
public class ScratchCodeDAO extends ServiceDAO {

    /**
     * Creates and return new ScratchCode
     *
     * @param em
     * @param scratchCodeCreator
     * @return
     * @throws KapuaException
     */
    public static ScratchCode create(EntityManager em, ScratchCodeCreator scratchCodeCreator) throws KapuaException {

        //
        // Crypto code (it's ok to do than if BCrypt is used when checking a provided scratch code against the stored one)
        String cryptedCode = crypt(scratchCodeCreator.getCode());

        //
        // Create code
        ScratchCodeImpl codeImpl = new ScratchCodeImpl(scratchCodeCreator.getScopeId(), scratchCodeCreator.getMfaCredentialOptionId(), cryptedCode);
        //
        // Do create
        return ServiceDAO.create(em, codeImpl);
    }

    /**
     * Update the provided ScratchCode
     *
     * @param em
     * @param code
     * @return
     * @throws KapuaException
     */
    public static ScratchCode update(EntityManager em, ScratchCode code) throws KapuaException {
        //
        // Update credential
        ScratchCodeImpl scratchCodeImpl = (ScratchCodeImpl) code;
        return ServiceDAO.update(em, ScratchCodeImpl.class, scratchCodeImpl);
    }

    /**
     * Find the ScratchCode by ScratchCode identifier
     *
     * @param em
     * @param scopeId
     * @param scratchCodeId
     * @return
     */
    public static ScratchCode find(EntityManager em, KapuaId scopeId, KapuaId scratchCodeId) {
        return ServiceDAO.find(em, ScratchCodeImpl.class, scopeId, scratchCodeId);
    }

    /**
     * Return the ScratchCode list matching the provided query
     *
     * @param em
     * @param scratchCodeQuery
     * @return
     * @throws KapuaException
     */
    public static ScratchCodeListResult query(EntityManager em, KapuaQuery scratchCodeQuery) throws KapuaException {
        return ServiceDAO.query(em, ScratchCode.class, ScratchCodeImpl.class, new ScratchCodeListResultImpl(), scratchCodeQuery);
    }

    /**
     * Return the ScratchCode count matching the provided query
     *
     * @param em
     * @param scratchCodeQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery scratchCodeQuery) throws KapuaException {
        return ServiceDAO.count(em, ScratchCode.class, ScratchCodeImpl.class, scratchCodeQuery);
    }

    /**
     * Delete the ScratchCode by ScratchCode identifier
     *
     * @param em
     * @param scopeId
     * @param scratchCodeId
     * @return deleted entity
     * @throws KapuaEntityNotFoundException If {@link ScratchCode} is now found.
     */
    public static ScratchCode delete(EntityManager em, KapuaId scopeId, KapuaId scratchCodeId) throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, ScratchCodeImpl.class, scopeId, scratchCodeId);
    }

    //
    // Private methods

    //
    private static String crypt(String scratchCodePlainCode) throws KapuaException {
        return AuthenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, scratchCodePlainCode);
    }
}
