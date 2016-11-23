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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.KapuaPredicate;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialPredicates;
import org.eclipse.kapua.service.authentication.credential.CredentialQuery;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.shiro.AuthenticationEntityManagerFactory;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;

/**
 * Credential service implementation.
 * 
 * @since 1.0
 *
 */
public class CredentialServiceImpl extends AbstractKapuaService implements CredentialService
{

    public CredentialServiceImpl() {
        super(AuthenticationEntityManagerFactory.getInstance());
    }

    @Override
    public Credential create(CredentialCreator credentialCreator)
        throws KapuaException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(credentialCreator, "credentialCreator");
        ArgumentValidator.notNull(credentialCreator.getScopeId(), "credentialCreator.scopeId");
        ArgumentValidator.notNull(credentialCreator.getUserId(), "credentialCreator.userId");
        ArgumentValidator.notNull(credentialCreator.getCredentialType(), "credentialCreator.credentialType");
        ArgumentValidator.notEmptyOrNull(credentialCreator.getCredentialPlainKey(), "credentialCreator.credentialKey");

        //
        // Check access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(CredentialDomain.CREDENTIAL, Actions.write, credentialCreator.getScopeId()));

        return entityManagerSession.onEntityManagerInsert(em -> {
            em.beginTransaction();
            Credential credential = CredentialDAO.create(em, credentialCreator);
            credential = CredentialDAO.find(em, credential.getId());
            em.commit();
            return credential;
        });
    }

    @Override
    public Credential update(Credential credential)
        throws KapuaException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(credential, "credentialCreator");
        ArgumentValidator.notNull(credential.getId(), "credentialCreator.id");
        ArgumentValidator.notNull(credential.getScopeId(), "credentialCreator.scopeId");
        ArgumentValidator.notNull(credential.getUserId(), "credentialCreator.userId");
        ArgumentValidator.notNull(credential.getCredentialType(), "credentialCreator.credentialType");
        ArgumentValidator.notEmptyOrNull(credential.getCredentialKey(), "credentialCreator.credentialKey");

        //
        // Check access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(CredentialDomain.CREDENTIAL, Actions.write, credential.getScopeId()));

        return entityManagerSession.onEntityManagerResult(em -> {
            Credential currentUser = CredentialDAO.find(em, credential.getId());
            if (currentUser == null) {
                throw new KapuaEntityNotFoundException(Credential.TYPE, credential.getId());
            }

            // Passing attributes??
            em.beginTransaction();
            CredentialDAO.update(em, credential);
            em.commit();

            return CredentialDAO.find(em, credential.getId());
        });
    }

    @Override
    public Credential find(KapuaId scopeId, KapuaId credentialId)
        throws KapuaException
    {
        // Validation of the fields
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(credentialId, "credentialId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(CredentialDomain.CREDENTIAL, Actions.read, scopeId));

        return entityManagerSession.onEntityManagerResult(em -> {
            return CredentialDAO.find(em, credentialId);
        });
    }

    @Override
    public CredentialListResult query(KapuaQuery<Credential> query)
        throws KapuaException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(CredentialDomain.CREDENTIAL, Actions.read, query.getScopeId()));

        return entityManagerSession.onEntityManagerResult(em -> {
            return CredentialDAO.query(em, query);
        });
    }

    @Override
    public long count(KapuaQuery<Credential> query)
        throws KapuaException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(CredentialDomain.CREDENTIAL, Actions.read, query.getScopeId()));

        return entityManagerSession.onEntityManagerResult(em -> {
            return CredentialDAO.count(em, query);
        });
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId credentialId)
        throws KapuaException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(credentialId, "credential.id");
        ArgumentValidator.notNull(scopeId, "credential.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(CredentialDomain.CREDENTIAL, Actions.delete, scopeId));

        entityManagerSession.onEntityManagerAction(em -> {
            if (CredentialDAO.find(em, credentialId) == null) {
                throw new KapuaEntityNotFoundException(Credential.TYPE, credentialId);
            }

            em.beginTransaction();
            CredentialDAO.delete(em, credentialId);
            em.commit();
        });
    }

    @Override
    public CredentialListResult findByUserId(KapuaId scopeId, KapuaId userId)
        throws KapuaException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(userId, "userId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(CredentialDomain.CREDENTIAL, Actions.read, scopeId));

        //
        // Build query
        CredentialQuery query = new CredentialQueryImpl(scopeId);
        KapuaPredicate predicate = new AttributePredicate<KapuaId>(CredentialPredicates.USER_ID, userId);
        query.setPredicate(predicate);

        //
        // Query and return result
        return query(query);
    }

}
