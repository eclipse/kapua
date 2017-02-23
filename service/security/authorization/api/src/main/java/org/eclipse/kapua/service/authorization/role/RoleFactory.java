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
 * {@link Role} object factory.
 * 
 * @since 1.0.0
 *
 */
public interface RoleFactory extends KapuaObjectFactory {

    /**
     * Instantiate a new {@link Role} implementing object with the provided scope id.
     * 
     * @param scopeId
     *            The scope id to set.
     * @return A instance of the implementing class of {@link Role}.
     * @since 1.0.0
     */
    public Role newRole(KapuaId scopeId);

    /**
     * Instantiate a new {@link RoleCreator} implementing object with the provided scope id.
     * 
     * @param scopeId
     *            The scope id to set.
     * @return A instance of the implementing class of {@link RoleCreator}.
     * @since 1.0.0
     */
    public RoleCreator newCreator(KapuaId scopeId);

    /**
     * Instantiate a new {@link RoleQuery} implementing object with the provided scope id.
     * 
     * @param scopeId
     *            The scope id to set.
     * @return A instance of the implementing class of {@link RoleQuery}.
     * @since 1.0.0
     */
    public RoleQuery newQuery(KapuaId scopeId);
    
    /**
     * Instantiate a new {@link RoleListResult} implementing object.
     * 
     * @return A instance of the implementing class of {@link RoleListResult}.
     * @since 1.0.0
     */
    public RoleListResult newRoleListResult();
    
    /**
     * Instantiate a new {@link RolePermission} implementing object.
     * 
     * @return A instance of the implementing class of {@link RolePermission}.
     * @since 1.0.0
     */
    public RolePermission newRolePermission();
}
