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
 * @since 1.0
 * 
 */
public interface AuthorizationService extends KapuaService
{
    /**
     * Return if the user (the current logged user retrieved by thread context) is allowed to perform the operation identified by provided the permission
     * 
     * @param permission
     * @return
     * @throws KapuaException if there is no logged context
     */
    public boolean isPermitted(Permission permission)
        throws KapuaException;

    /**
     * Check if the user (the current logged user retrieved by thread context) is allowed to perform the operation identified by provided the permission
     * 
     * @param permission
     * 
     * @throws KapuaException if there is no logged context or if the user has no right for the provided permission
     */
    public void checkPermission(Permission permission)
        throws KapuaException;
}
