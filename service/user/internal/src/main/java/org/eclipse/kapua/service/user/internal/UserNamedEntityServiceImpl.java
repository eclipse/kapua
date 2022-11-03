/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial implementation
 *******************************************************************************/
package org.eclipse.kapua.service.user.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManagerContainer;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.service.internal.cache.NamedEntityCache;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserDomains;
import org.eclipse.kapua.service.user.UserNamedEntityService;

import javax.inject.Inject;

public class UserNamedEntityServiceImpl extends AbstractKapuaService implements UserNamedEntityService {

    @Inject
    public UserNamedEntityServiceImpl(
            UserEntityManagerFactory userEntityManagerFactory,
            UserCacheFactory userCacheFactory,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService) {
        super(userEntityManagerFactory, userCacheFactory);
        this.permissionFactory = permissionFactory;
        this.authorizationService = authorizationService;
    }

    private final PermissionFactory permissionFactory;
    private final AuthorizationService authorizationService;

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

    private User checkReadAccess(User user) throws KapuaException {
        if (user != null) {
            authorizationService.checkPermission(permissionFactory.newPermission(UserDomains.USER_DOMAIN, Actions.read, user.getScopeId()));
        }
        return user;
    }
}
