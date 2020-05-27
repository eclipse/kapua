/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.user.internal;

import org.eclipse.kapua.KapuaDuplicateExternalIdException;
import org.eclipse.kapua.KapuaDuplicateNameException;
import org.eclipse.kapua.KapuaDuplicateNameInAnotherAccountError;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.KapuaMaxNumberOfItemsReachedException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableResourceLimitedService;
import org.eclipse.kapua.commons.jpa.EntityManagerContainer;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.service.internal.cache.NamedEntityCache;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.CommonsValidationRegex;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserAttributes;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserDomains;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.UserStatus;
import org.eclipse.kapua.service.user.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Objects;

/**
 * {@link UserService} implementation.
 */
@KapuaProvider
public class UserServiceImpl extends AbstractKapuaConfigurableResourceLimitedService<User, UserCreator, UserService, UserListResult, UserQuery, UserFactory> implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Inject
    private AuthorizationService authorizationService;

    @Inject
    private PermissionFactory permissionFactory;

    /**
     * Constructor
     */
    public UserServiceImpl() {
        super(UserService.class.getName(), UserDomains.USER_DOMAIN, UserEntityManagerFactory.getInstance(),
                UserCacheFactory.getInstance(), UserService.class, UserFactory.class);
    }

    @Override
    public User create(UserCreator userCreator) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(userCreator.getScopeId().getId(), "userCreator.scopeId");
        ArgumentValidator.notEmptyOrNull(userCreator.getName(), "userCreator.name");
        ArgumentValidator.match(userCreator.getName(), CommonsValidationRegex.NAME_REGEXP, "userCreator.name");
        ArgumentValidator.match(userCreator.getEmail(), CommonsValidationRegex.EMAIL_REGEXP, "userCreator.email");
        ArgumentValidator.notNull(userCreator.getUserType(), "userCreator.userType");
        ArgumentValidator.notNull(userCreator.getUserStatus(), "userCreator.userStatus");

        if (userCreator.getUserType() == UserType.EXTERNAL) {
            ArgumentValidator.notEmptyOrNull(userCreator.getExternalId(), "userCreator.externalId");
        } else if (userCreator.getUserType() == UserType.INTERNAL) {
            ArgumentValidator.isEmptyOrNull(userCreator.getExternalId(), "userCreator.externalId");
        }

        int remainingChildEntities = allowedChildEntities(userCreator.getScopeId());
        if (remainingChildEntities <= 0) {
            LOGGER.info("Exceeded child limit - remaining: {}", remainingChildEntities);
            throw new KapuaMaxNumberOfItemsReachedException("Users");
        }

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(UserDomains.USER_DOMAIN, Actions.write, userCreator.getScopeId()));

        //
        // Check duplicate name
        UserQuery query = new UserQueryImpl(userCreator.getScopeId());
        query.setPredicate(query.attributePredicate(UserAttributes.NAME, userCreator.getName()));
        if (count(query) > 0) {
            throw new KapuaDuplicateNameException(userCreator.getName());
        }

        User userByName = KapuaSecurityUtils.doPrivileged(() -> findByName(userCreator.getName()));
        if (userByName != null) {
            throw new KapuaDuplicateNameInAnotherAccountError(userCreator.getName());
        }

        if (userCreator.getUserType() == UserType.EXTERNAL) {
            // Check duplicate externalId
            User userByExternalId = KapuaSecurityUtils.doPrivileged(() -> findByExternalId(userCreator.getExternalId()));
            if (userByExternalId != null) {
                throw new KapuaDuplicateExternalIdException(userCreator.getExternalId());
            }
        }

        //
        // Do create
        return entityManagerSession.doTransactedAction(EntityManagerContainer.<User>create().onResultHandler(em -> UserDAO.create(em,
                userCreator)));
    }

    @Override
    //@RaiseServiceEvent
    public User update(User user) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(user.getId().getId(), "user.id");
        ArgumentValidator.notNull(user.getScopeId(), "user.scopeId");
        ArgumentValidator.notEmptyOrNull(user.getName(), "user.name");
        ArgumentValidator.match(user.getName(), CommonsValidationRegex.NAME_REGEXP, "user.name");
        ArgumentValidator.match(user.getEmail(), CommonsValidationRegex.EMAIL_REGEXP, "user.email");
        ArgumentValidator.notNull(user.getUserType(), "user.userType");

        if (user.getUserType() == UserType.EXTERNAL) {
            ArgumentValidator.notEmptyOrNull(user.getExternalId(), "user.externalId");
        } else if (user.getUserType() == UserType.INTERNAL) {
            ArgumentValidator.isEmptyOrNull(user.getExternalId(), "user.externalId");
        }

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(UserDomains.USER_DOMAIN, Actions.write, user.getScopeId()));

        //
        // Check existence
        User currentUser = find(user.getScopeId(), user.getId());
        if (currentUser == null) {
            throw new KapuaEntityNotFoundException(User.TYPE, user.getId());
        }

        if (user.getExpirationDate() != null || !currentUser.getName().equals(user.getName())) {
            //
            // Check not deleting environment admin
            validateSystemUser(user.getName());
        }
        if (user.getId().equals(KapuaSecurityUtils.getSession().getUserId())) {
            if (user.getStatus().equals(UserStatus.DISABLED)) {
                throw new KapuaIllegalArgumentException("status", user.getStatus().name());
            }
        }
        if (!Objects.equals(currentUser.getUserType(), user.getUserType())) {
            throw new KapuaIllegalArgumentException("userType", user.getUserType().toString());
        }
        if (!Objects.equals(currentUser.getExternalId(), user.getExternalId())) {
            throw new KapuaIllegalArgumentException("externalId", user.getExternalId());
        }

        //
        // Do update
        return entityManagerSession.doTransactedAction(EntityManagerContainer.<User>create().onResultHandler(em -> UserDAO.update(em, user))
                .onBeforeHandler(() -> {
                    entityCache.remove(null, user);
                    return null;
                }));
    }

    @Override
    //@RaiseServiceEvent
    public void delete(KapuaId scopeId, KapuaId userId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(userId.getId(), "user.id");
        ArgumentValidator.notNull(scopeId.getId(), "user.scopeId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(UserDomains.USER_DOMAIN, Actions.delete, scopeId));

        //
        // Check existence
        User user = find(scopeId, userId);
        if (user == null) {
            throw new KapuaEntityNotFoundException(User.TYPE, userId);
        }

        //
        // Check not deleting environment admin
        validateSystemUser(user.getName());

        //
        // Check not deleting self
        validateSelf(user);

        //
        // Do  delete
        entityManagerSession.doTransactedAction(EntityManagerContainer.<User>create().onResultHandler(em -> UserDAO.delete(em, scopeId, userId))
                .onAfterHandler((emptyParam) -> entityCache.remove(scopeId, userId)));
    }

    @Override
    public void delete(User user) throws KapuaException {
        ArgumentValidator.notNull(user, "user");
        delete(user.getScopeId(), user.getId());
    }

    @Override
    public User find(KapuaId scopeId, KapuaId userId)
            throws KapuaException {
        // Validation of the fields
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(userId, "userId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(UserDomains.USER_DOMAIN, Actions.read, scopeId));

        // Do the find
        return entityManagerSession.doAction(EntityManagerContainer.<User>create().onResultHandler(em -> UserDAO.find(em, scopeId, userId))
                .onBeforeHandler(() -> (User) entityCache.get(scopeId, userId))
                .onAfterHandler((entity) -> entityCache.put(entity))
        );
    }

    @Override
    public User findByName(String name) throws KapuaException {
        //
        // Validation of the fields
        ArgumentValidator.notEmptyOrNull(name, "name");

        //
        // Do the find
        return entityManagerSession.doAction(EntityManagerContainer.<User>create().onResultHandler(em -> checkReadAccess(UserDAO.findByName(em, name)))
                .onBeforeHandler(() -> checkReadAccess((User) ((NamedEntityCache) entityCache).get(null, name)))
                .onAfterHandler((entity) -> entityCache.put(entity)));
    }

    @Override
    public User findByExternalId(String externalId) throws KapuaException {
        //
        // Validation of the fields
        ArgumentValidator.notEmptyOrNull(externalId, "externalId");

        //
        // Do the find
        return entityManagerSession.doAction(EntityManagerContainer.<User>create().onResultHandler(em -> checkReadAccess(UserDAO.findByExternalId(em, externalId)))
                .onAfterHandler((entity) -> entityCache.put(entity)));
    }

    @Override
    public UserListResult query(KapuaQuery query)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(UserDomains.USER_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.doAction(EntityManagerContainer.<UserListResult>create().onResultHandler(em -> UserDAO.query(em, query)));
    }

    @Override
    public long count(KapuaQuery query)
            throws KapuaException {
        //
        // Argument Validator
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(UserDomains.USER_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return entityManagerSession.doAction(EntityManagerContainer.<Long>create().onResultHandler(em -> UserDAO.count(em, query)));
    }

    // -----------------------------------------------------------------------------------------
    //
    // Private Methods
    //
    // -----------------------------------------------------------------------------------------

    private User checkReadAccess(User user) throws KapuaException {
        if (user != null) {
            authorizationService.checkPermission(permissionFactory.newPermission(UserDomains.USER_DOMAIN, Actions.read, user.getScopeId()));
        }
        return user;
    }

    private void validateSystemUser(String name) throws KapuaException {
        String adminUsername = SystemSetting.getInstance().getString(SystemSettingKey.SYS_ADMIN_USERNAME);

        if (adminUsername.equals(name)) {
            throw new KapuaIllegalArgumentException("name", adminUsername);
        }
    }

    private void validateSelf(User user) throws KapuaException {
        if (user.getId().equals(KapuaSecurityUtils.getSession().getUserId())) {
            throw new KapuaIllegalArgumentException("name", user.getName());
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
        UserQuery query = new UserQueryImpl(accountId);
        UserListResult usersToDelete = query(query);

        for (User u : usersToDelete.getItems()) {
            delete(u.getScopeId(), u.getId());
        }
    }

}
