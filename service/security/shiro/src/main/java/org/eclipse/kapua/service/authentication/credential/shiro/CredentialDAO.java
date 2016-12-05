/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.credential.shiro;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.shiro.utils.AuthenticationUtils;

/**
 * Credential DAO.
 * 
 * @since 1.0
 *
 */
public class CredentialDAO extends ServiceDAO
{

    /**
     * Creates and return new credential
     * 
     * @param em
     * @param credentialCreator
     * @return
     * @throws KapuaException
     */
    public static Credential create(EntityManager em, CredentialCreator credentialCreator)
        throws KapuaException
    {
        //
        // Create User
        CredentialImpl credentialImpl = new CredentialImpl(credentialCreator.getScopeId(),
                                                           credentialCreator.getUserId(),
                                                           credentialCreator.getCredentialType(),
                                                           AuthenticationUtils.cryptCredential(credentialCreator.getCredentialPlainKey()));

        return ServiceDAO.create(em, credentialImpl);
    }

    /**
     * Update the provided credential
     * 
     * @param em
     * @param credential
     * @return
     * @throws KapuaException
     */
    public static Credential update(EntityManager em, Credential credential)
        throws KapuaException
    {
        //
        // Update user
        CredentialImpl credentialImpl = (CredentialImpl) credential;
        credential.setCredentialKey(AuthenticationUtils.cryptCredential(credential.getCredentialKey()));
        return ServiceDAO.update(em, CredentialImpl.class, credentialImpl);
    }

    /**
     * Delete the credential by credential identifier
     * 
     * @param em
     * @param credentialId
     */
    public static void delete(EntityManager em, KapuaId credentialId)
    {
        ServiceDAO.delete(em, CredentialImpl.class, credentialId);
    }

    /**
     * Find the credential by credential identifier
     * 
     * @param em
     * @param credentialId
     * @return
     */
    public static Credential find(EntityManager em, KapuaId credentialId)
    {
        return em.find(CredentialImpl.class, credentialId);
    }

    /**
     * Return the credential list matching the provided query
     * 
     * @param em
     * @param credentialQuery
     * @return
     * @throws KapuaException
     */
    public static CredentialListResult query(EntityManager em, KapuaQuery<Credential> credentialQuery)
        throws KapuaException
    {
        return ServiceDAO.query(em, Credential.class, CredentialImpl.class, new CredentialListResultImpl(), credentialQuery);
    }

    /**
     * Return the credential count matching the provided query
     * 
     * @param em
     * @param credentialQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery<Credential> credentialQuery)
        throws KapuaException
    {
        return ServiceDAO.count(em, Credential.class, CredentialImpl.class, credentialQuery);
    }

}
