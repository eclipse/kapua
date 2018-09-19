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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserListResult;

/**
 * {@link User} {@link ServiceDAO}
 *
 * @since 1.0.0
 */
public class UserDAO extends ServiceDAO {

    /**
     * Creates and return new {@link User}
     *
     * @param em
     * @param userCreator
     * @return
     * @since 1.0.0
     */
    public static User create(EntityManager em, UserCreator userCreator) {
        //
        // Create User
        UserImpl userImpl = new UserImpl(userCreator.getScopeId(), userCreator.getName());

        userImpl.setDisplayName(userCreator.getDisplayName());
        userImpl.setEmail(userCreator.getEmail());
        userImpl.setPhoneNumber(userCreator.getPhoneNumber());
        userImpl.setUserType(userCreator.getUserType());
        userImpl.setExternalId(userCreator.getExternalId());
        userImpl.setStatus(userCreator.getUserStatus());
        userImpl.setExpirationDate(userCreator.getExpirationDate());

        return ServiceDAO.create(em, userImpl);
    }

    /**
     * Updates the provided {@link User}
     *
     * @param em
     * @param user
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    public static User update(EntityManager em, User user) throws KapuaException {
        UserImpl userImpl = (UserImpl) user;

        return ServiceDAO.update(em, UserImpl.class, userImpl);
    }

    /**
     * Finds the {@link User} by {@link User} identifier
     *
     * @param em
     * @param scopeId
     * @param userId
     * @return
     * @since 1.0.0
     */
    public static User find(EntityManager em, KapuaId scopeId, KapuaId userId) {
        return ServiceDAO.find(em, UserImpl.class, scopeId, userId);
    }

    /**
     * Finds the {@link User} by the {@link org.eclipse.kapua.service.user.UserAttributes#NAME}
     *
     * @param em
     * @param name
     * @return
     */
    public static User findByName(EntityManager em, String name) {
        return ServiceDAO.findByField(em, UserImpl.class, "name", name);
    }

    /**
     * Finds the {@link User} by the {@link org.eclipse.kapua.service.user.UserAttributes} external id
     *
     * @param em         the entity manager to use
     * @param externalId id the external ID so search for
     * @return the user record, may be {@code null}
     */
    public static User findByExternalId(final EntityManager em, final String externalId) {
        return ServiceDAO.findByField(em, UserImpl.class, "externalId", externalId);
    }

    /**
     * Returns the user list matching the provided query
     *
     * @param em
     * @param userPermissionQuery
     * @return
     * @throws KapuaException
     */
    public static UserListResult query(EntityManager em, KapuaQuery<User> userPermissionQuery)
            throws KapuaException {
        return ServiceDAO.query(em, User.class, UserImpl.class, new UserListResultImpl(), userPermissionQuery);
    }

    /**
     * Returns the user count matching the provided query
     *
     * @param em
     * @param userPermissionQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery<User> userPermissionQuery)
            throws KapuaException {
        return ServiceDAO.count(em, User.class, UserImpl.class, userPermissionQuery);
    }

    /**
     * Deletes the user by user identifier
     *
     * @param em
     * @param scopeId
     * @param userId
     * @throws KapuaEntityNotFoundException If {@link User} is not found.
     */
    public static void delete(EntityManager em, KapuaId scopeId, KapuaId userId)
            throws KapuaEntityNotFoundException {
        ServiceDAO.delete(em, UserImpl.class, scopeId, userId);
    }

}
