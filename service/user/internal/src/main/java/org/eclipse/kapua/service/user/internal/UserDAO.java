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
package org.eclipse.kapua.service.user.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserListResult;

/**
 * User DAO
 * 
 * @since 1.0
 *
 */
public class UserDAO extends ServiceDAO {

    /**
     * Creates and return new User
     * 
     * @param em
     * @param userCreator
     * @return
     * @throws KapuaException
     */
    public static User create(EntityManager em, UserCreator userCreator)
            throws KapuaException {
        //
        // Create User
        UserImpl userImpl = new UserImpl(userCreator.getScopeId(),
                userCreator.getName());

        userImpl.setDisplayName(userCreator.getDisplayName());
        userImpl.setEmail(userCreator.getEmail());
        userImpl.setPhoneNumber(userCreator.getPhoneNumber());
        userImpl.setUserType(userCreator.getUserType());
        userImpl.setExternalId(userCreator.getExternalId());

        return ServiceDAO.create(em, userImpl);
    }

    /**
     * Updates the provided user
     * 
     * @param em
     * @param user
     * @return
     * @throws KapuaException
     */
    public static User update(EntityManager em, User user)
            throws KapuaException {
        //
        // Update user
        UserImpl userImpl = (UserImpl) user;

        return ServiceDAO.update(em, UserImpl.class, userImpl);
    }

    /**
     * Deletes the user by user identifier
     * 
     * @param em
     * @param userId
     */
    public static void delete(EntityManager em, KapuaId userId) {
        ServiceDAO.delete(em, UserImpl.class, userId);
    }

    /**
     * Finds the user by user identifier
     * 
     * @param em
     * @param userId
     * @return
     */
    public static User find(EntityManager em, KapuaId userId) {
        return em.find(UserImpl.class, userId);
    }

    /**
     * Finds the user by name
     * 
     * @param em
     * @param name
     * @return
     */
    public static User findByName(EntityManager em, String name) {
        return ServiceDAO.findByField(em, UserImpl.class, "name", name);
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

}
