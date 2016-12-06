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
package org.eclipse.kapua.service.authorization;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.authorization.permission.Permission;

/**
 * AuthenticationService exposes APIs to manage User object under an Account.<br>
 * It includes APIs to create, update, find, list and delete Users.<br>
 * Instances of the UserService can be acquired through the ServiceLocator.
 * 
 * @since 1.0.0
 * 
 */
public interface AuthorizationService extends KapuaService {

    /**
     * Returns if the user (the current logged user retrieved by thread context) is allowed to perform the operation identified by provided the permission.
     * 
     * @param permission
     *            The permission to check.
     * @return {@code true} if the current user has the given permission, {@code false} otherwise.
     * @throws KapuaException
     *             If there is no logged context.
     * 
     * @since 1.0.0
     */
    public boolean isPermitted(Permission permission)
            throws KapuaException;

    /**
     * Checks if the user (the current logged user retrieved by thread context) is allowed to perform the operation identified by provided the permission.
     * 
     * @param permission
     *            The permission to check.
     * @throws KapuaException
     *             if there is no logged context or if the user has no right for the provided permission.
     * @since 1.0.0
     */
    public void checkPermission(Permission permission)
            throws KapuaException;
}
