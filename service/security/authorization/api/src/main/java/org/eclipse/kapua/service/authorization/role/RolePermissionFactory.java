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
package org.eclipse.kapua.service.authorization.role;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link RolePermission} object factory.
 * 
 * @since 1.0.0
 *
 */
public interface RolePermissionFactory extends KapuaObjectFactory {

    /**
     * Instantiate a new {@link RolePermission} implementing object with the provided scope id.
     * 
     * @param scopeId
     *            The scope id to set.
     * @return A instance of the implementing class of {@link RolePermission}.
     * @since 1.0.0
     */
    public RolePermission newRolePermission(KapuaId scopeId);

    /**
     * Instantiate a new {@link RolePermissionCreator} implementing object with the provided scope id.
     * 
     * @param scopeId
     *            The scope id to set.
     * @return A instance of the implementing class of {@link RolePermissionCreator}.
     * @since 1.0.0
     */
    public RolePermissionCreator newCreator(KapuaId scopeId);

    /**
     * Instantiate a new {@link RolePermissionQuery} implementing object with the provided scope id.
     * 
     * @param scopeId
     *            The scope id to set.
     * @return A instance of the implementing class of {@link RolePermissionQuery}.
     * @since 1.0.0
     */
    public RolePermissionQuery newQuery(KapuaId scopeId);

    /**
     * Instantiate a new {@link RolePermissionListResult} implementing object.
     * 
     * @return A instance of the implementing class of {@link RolePermissionListResult}.
     * @since 1.0.0
     */
    public RolePermissionListResult newRolePermissionListResult();

    /**
     * Instantiate a new {@link RolePermission} implementing object.
     * 
     * @return A instance of the implementing class of {@link RolePermission}.
     * @since 1.0.0
     */
    public RolePermission newRolePermission();
}
