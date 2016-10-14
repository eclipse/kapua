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
package org.eclipse.kapua.service.user;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaNamedEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

/**
 * UserService exposes APIs to manage User object under an Account.<br>
 * It includes APIs to create, update, find, list and delete Users.<br>
 * Instances of the UserService can be acquired through the ServiceLocator.
 * 
 * @since 1.0
 * 
 */
public interface UserService extends KapuaEntityService<User, UserCreator>,
                             KapuaUpdatableEntityService<User>,
                             KapuaNamedEntityService<User>,
                             KapuaConfigurableService
{
    /**
     * Creates a new user under the account specified in the UserCreator.<br>
     * The returned User object does not have its access information, roles
     * and permissions, loaded.
     *
     * @param userCreator
     * @return created User
     * @throws KapuaException
     */
    public User create(UserCreator userCreator)
        throws KapuaException;

    /**
     * Updates an User in the database and returns the refreshed/reloaded entity instance.<br>
     * The returned User object does not have its access information, roles
     * and permissions, loaded.
     *
     * @param user to be update
     * @return
     * @throws KapuaException
     */
    public User update(User user)
        throws KapuaException;

    /**
     * Delete the supplied User.
     *
     * @param user
     * @throws KapuaException
     */
    public void delete(User user)
        throws KapuaException;

    /**
     * Returns the User with the specified Id; returns null if the user is not found.<br>
     * <b>The API does not perform any access control check and it is meant for internal use.</b>
     *
     * @param userId
     * @return
     * @throws KapuaException
     */
    public User find(KapuaId accountId, KapuaId userId)
        throws KapuaException;

    /**
     * Returns the User with the specified username; returns null if the user is not found.
     *
     * @return
     * @throws KapuaException
     */
    public User findByName(String name)
        throws KapuaException;

    /**
     * Queries for all users
     */
    public UserListResult query(KapuaQuery<User> query)
        throws KapuaException;
}
