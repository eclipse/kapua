/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *
 *******************************************************************************/
package org.eclipse.kapua.service.user.internal;

import java.util.Objects;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.UserType;

/**
 * User service implementation.
 * 
 * @since 1.0
 *
 */
@KapuaProvider
public class UserServiceImpl extends AbstractKapuaConfigurableService implements UserService {

    private static final long serialVersionUID = 4319929212203916781L;
    private final KapuaLocator locator = KapuaLocator.getInstance();

    private static final Domain userDomain = new UserDomain();

    /**
     * Constructor
     */
    public UserServiceImpl() {
        super(UserService.class.getName(), userDomain, UserEntityManagerFactory.getInstance());
    }

    @Override
    public User create(UserCreator userCreator)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(userCreator.getScopeId().getId(), "scopeId");
        ArgumentValidator.notEmptyOrNull(userCreator.getName(), "name");
        // ArgumentValidator.notEmptyOrNull(userCreator.getRawPassword(), "rawPassword");
        ArgumentValidator.match(userCreator.getName(), ArgumentValidator.NAME_REGEXP, "name");
        // ArgumentValidator.match(userCreator.getRawPassword(), ArgumentValidator.PASSWORD_REGEXP, "rawPassword");
        ArgumentValidator.match(userCreator.getEmail(), ArgumentValidator.EMAIL_REGEXP, "email");
        ArgumentValidator.notNull(userCreator.getUserType(), "userType");
        if (userCreator.getUserType() != UserType.INTERNAL) {
            ArgumentValidator.notEmptyOrNull(userCreator.getExternalId(), "externalId");
        } else {
            ArgumentValidator.isEmptyOrNull(userCreator.getExternalId(), "externalId");
        }

        //
        // Check Access
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(userDomain, Actions.write, userCreator.getScopeId()));

        return entityManagerSession.onTransactedInsert(em -> UserDAO.create(em, userCreator));
    }

    @Override
    public User update(User user)
            throws KapuaException {
        // Validation of the fields
        ArgumentValidator.notNull(user.getId().getId(), "id");
        ArgumentValidator.notNull(user.getScopeId().getId(), "accountId");
        ArgumentValidator.notEmptyOrNull(user.getName(), "name");
        ArgumentValidator.match(user.getName(), ArgumentValidator.NAME_REGEXP, "name");
        // ArgumentValidator.match(user.getRawPassword(), ArgumentValidator.PASSWORD_REGEXP, "rawPassword");
        ArgumentValidator.match(user.getEmail(), ArgumentValidator.EMAIL_REGEXP, "email");
        if (user.getUserType() != UserType.INTERNAL) {
            ArgumentValidator.notEmptyOrNull(user.getExternalId(), "externalId");
        } else {
            ArgumentValidator.isEmptyOrNull(user.getExternalId(), "externalId");
        }
        validateSystemUser(user.getName());

        //
        // Check Access
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(userDomain, Actions.write, user.getScopeId()));

        //
        // Do update
        return entityManagerSession.onTransactedResult(em -> {
            User currentUser = UserDAO.find(em, user.getId());
            if (currentUser == null) {
                throw new KapuaEntityNotFoundException(User.TYPE, user.getId());
            }
            if (!Objects.equals(currentUser.getUserType(), user.getUserType())) {
                throw new KapuaIllegalArgumentException("userType", user.getUserType().toString());
            }
            if (!Objects.equals(currentUser.getExternalId(), user.getExternalId())) {
                throw new KapuaIllegalArgumentException("externalId", user.getExternalId());
            }
            return UserDAO.update(em, user);
        });
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId userId) throws KapuaException {
        // Validation of the fields
        ArgumentValidator.notNull(userId.getId(), "id");
        ArgumentValidator.notNull(scopeId.getId(), "accountId");

        //
        // Check Access
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(userDomain, Actions.write, scopeId));

        // Do the delete
        entityManagerSession.onTransactedAction(em -> {
            // Entity needs to be loaded in the context of the same EntityManger to be able to delete it afterwards
            User userx = UserDAO.find(em, userId);
            if (userx == null) {
                throw new KapuaEntityNotFoundException(User.TYPE, userId);
            }
            User user = find(scopeId, userId);
            validateSystemUser(user.getName());

            // Ensure this is not the last admin for the account
            // FIXME-KAPUA: Ask the Authorization Service
            // UserDAO.checkForLastAccountAdministratorDelete(em, userx);

            UserDAO.delete(em, userId);
        });
    }

    @Override
    public void delete(User user) throws KapuaException {
        ArgumentValidator.notNull(user, "user");
        delete(user.getScopeId(), user.getId());
    }

    @Override
    public User find(KapuaId accountId, KapuaId userId)
            throws KapuaException {
        // Validation of the fields
        ArgumentValidator.notNull(accountId.getId(), "accountId");
        ArgumentValidator.notNull(userId.getId(), "id");

        //
        // Check Access
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(userDomain, Actions.read, accountId));

        // Do the find
        return entityManagerSession.onResult(em -> UserDAO.find(em, userId));
    }

    @Override
    public User findByName(String name)
            throws KapuaException {
        // Validation of the fields
        ArgumentValidator.notEmptyOrNull(name, "name");

        // Do the find
        return entityManagerSession.onResult(em -> {
            User user = UserDAO.findByName(em, name);
            //
            // Check Access
            if (user != null) {
                AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
                PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
                authorizationService.checkPermission(permissionFactory.newPermission(userDomain, Actions.read, user.getScopeId()));
            }
            return user;
        });
    }

    @Override
    public UserListResult query(KapuaQuery<User> query)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(userDomain, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> UserDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<User> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(userDomain, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> UserDAO.count(em, query));
    }

    // -----------------------------------------------------------------------------------------
    //
    // Private Methods
    //
    // -----------------------------------------------------------------------------------------

    private void validateSystemUser(String name)
            throws KapuaException {
        // FIXME-KAPUA: AuthenticationService get system user name via config
        if ("kapua-sys".equals(name)) {
            throw new KapuaIllegalArgumentException("name", "kapua-sys");
        }
    }

}
