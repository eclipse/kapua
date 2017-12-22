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
 *******************************************************************************/
package org.eclipse.kapua.service.user.internal;

import static org.eclipse.kapua.commons.util.ArgumentValidator.notEmptyOrNull;

import java.util.Objects;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableResourceLimitedService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.event.ServiceEvent;
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
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User service implementation.
 */
@KapuaProvider
public class UserServiceImpl extends AbstractKapuaConfigurableResourceLimitedService<User, UserCreator, UserService, UserListResult, UserQuery, UserFactory> implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final Domain USER_DOMAIN = new UserDomain();

    @Inject
    private AuthorizationService authorizationService;

    @Inject
    private PermissionFactory permissionFactory;

    /**
     * Constructor
     */
    public UserServiceImpl() {
        super(UserService.class.getName(), USER_DOMAIN, UserEntityManagerFactory.getInstance(), UserService.class, UserFactory.class);
    }

    @Override
    //@RaiseServiceEvent
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
        ArgumentValidator.notNull(userCreator.getUserStatus(), "userStatus");
        if (userCreator.getUserType() != UserType.INTERNAL) {
            ArgumentValidator.notEmptyOrNull(userCreator.getExternalId(), "externalId");
        } else {
            ArgumentValidator.isEmptyOrNull(userCreator.getExternalId(), "externalId");
        }

        final int remainingChildEntities = allowedChildEntities(userCreator.getScopeId());
        if (remainingChildEntities <= 0) {
            LOGGER.info("Exceeded child limit - remaining: {}", remainingChildEntities);
            throw new KapuaIllegalArgumentException("scopeId", "max users reached");
        }

        //
        // Check Access
        this.authorizationService.checkPermission(this.permissionFactory.newPermission(USER_DOMAIN, Actions.write, userCreator.getScopeId()));

        return entityManagerSession.onTransactedInsert(em -> UserDAO.create(em, userCreator));
    }

    @Override
    //@RaiseServiceEvent
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
        this.authorizationService.checkPermission(this.permissionFactory.newPermission(USER_DOMAIN, Actions.write, user.getScopeId()));

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
    //@RaiseServiceEvent
    public void delete(KapuaId scopeId, KapuaId userId) throws KapuaException {
        // Validation of the fields
        ArgumentValidator.notNull(userId.getId(), "id");
        ArgumentValidator.notNull(scopeId.getId(), "accountId");

        //
        // Check Access
        this.authorizationService.checkPermission(this.permissionFactory.newPermission(USER_DOMAIN, Actions.write, scopeId));

        // Do the delete
        entityManagerSession.onTransactedAction(em -> {
            User user = UserDAO.find(em, userId);
            if (user == null) {
                throw new KapuaEntityNotFoundException(User.TYPE, userId);
            }
            validateSystemUser(user.getName());

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
        this.authorizationService.checkPermission(this.permissionFactory.newPermission(USER_DOMAIN, Actions.read, accountId));

        // Do the find
        return entityManagerSession.onResult(em -> UserDAO.find(em, userId));
    }

    @Override
    public User findByName(final String name) throws KapuaException {

        // Validation of the fields

        ArgumentValidator.notEmptyOrNull(name, "name");

        // Do the find

        return entityManagerSession.onResult(em -> checkReadAccess(UserDAO.findByName(em, name)));
    }

    @Override
    public User findByExternalId(final String externalId) throws KapuaException {

        // Validation of the fields

        notEmptyOrNull(externalId, "externalId");

        // Do the find

        return entityManagerSession.onResult(em -> checkReadAccess(UserDAO.findByExternalId(em, externalId)));
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
        this.authorizationService.checkPermission(this.permissionFactory.newPermission(USER_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> UserDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<User> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        this.authorizationService.checkPermission(this.permissionFactory.newPermission(USER_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> UserDAO.count(em, query));
    }

    // -----------------------------------------------------------------------------------------
    //
    // Private Methods
    //
    // -----------------------------------------------------------------------------------------

    private User checkReadAccess(final User user) throws KapuaException {
        if (user != null) {
            this.authorizationService.checkPermission(this.permissionFactory.newPermission(USER_DOMAIN, Actions.read, user.getScopeId()));
        }
        return user;
    }

    private void validateSystemUser(String name)
            throws KapuaException {
        // FIXME-KAPUA: AuthenticationService get system user name via config
        if ("kapua-sys".equals(name)) {
            throw new KapuaIllegalArgumentException("name", "kapua-sys");
        }
    }

    //@ListenServiceEvent(fromAddress = "account")
    public void onKapuaEvent(ServiceEvent kapuaEvent) throws KapuaException {
        if (kapuaEvent == null) {
            // service bus error. Throw some exception?
        }
        LOGGER.info("UserService: received kapua event from {}, operation {}", kapuaEvent.getService(), kapuaEvent.getOperation());
        if ("account".equals(kapuaEvent.getService()) && "delete".equals(kapuaEvent.getOperation())) {
            deleteUserByAccountId(kapuaEvent.getScopeId(), kapuaEvent.getEntityId());
        }
    }

    private void deleteUserByAccountId(KapuaId scopeId, KapuaId accountId) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        UserFactory userFactory = locator.getFactory(UserFactory.class);
        UserQuery query = userFactory.newQuery(accountId);
        UserListResult usersToDelete = query(query);
        for (User u : usersToDelete.getItems()) {
            delete(u.getScopeId(), u.getId());
        }
    }

}
