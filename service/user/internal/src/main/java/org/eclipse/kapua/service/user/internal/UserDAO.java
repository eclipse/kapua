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
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.user.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.KapuaNamedEntityAttributes;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserAttributes;
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
        userImpl.setExternalUsername(userCreator.getExternalUsername());
        userImpl.setStatus(userCreator.getStatus());
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
        return ServiceDAO.findByField(em, UserImpl.class, KapuaNamedEntityAttributes.NAME, name);
    }

    /**
     * Finds the {@link User} by the {@link UserAttributes#EXTERNAL_ID}
     *
     * @param em         the entity manager to use
     * @param externalId id the external ID so search for
     * @return the user record, may be {@code null}
     */
    public static User findByExternalId(final EntityManager em, final String externalId) {
        return ServiceDAO.findByField(em, UserImpl.class, UserAttributes.EXTERNAL_ID, externalId);
    }


    /**
     * Finds the {@link User} by the {@link UserAttributes#EXTERNAL_USERNAME}.
     *
     * @param em               the entity manager to use
     * @param externalUsername id the external username so search for
     * @return the user record, may be {@code null}
     * @since 2.0.0
     */
    public static User findByExternalUsername(EntityManager em, String externalUsername) {
        return ServiceDAO.findByField(em, UserImpl.class, UserAttributes.EXTERNAL_USERNAME, externalUsername);
    }

    /**
     * Returns the user list matching the provided query
     *
     * @param em
     * @param userPermissionQuery
     * @return
     * @throws KapuaException
     */
    public static UserListResult query(EntityManager em, KapuaQuery userPermissionQuery)
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
    public static long count(EntityManager em, KapuaQuery userPermissionQuery)
            throws KapuaException {
        return ServiceDAO.count(em, User.class, UserImpl.class, userPermissionQuery);
    }

    /**
     * Deletes the user by user identifier
     *
     * @param em
     * @param scopeId
     * @param userId
     * @return the deleted {@link User}
     * @throws KapuaEntityNotFoundException If {@link User} is not found.
     */
    public static User delete(EntityManager em, KapuaId scopeId, KapuaId userId)
            throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, UserImpl.class, scopeId, userId);
    }

}
