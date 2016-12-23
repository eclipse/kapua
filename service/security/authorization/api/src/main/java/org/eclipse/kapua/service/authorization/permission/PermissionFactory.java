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
package org.eclipse.kapua.service.authorization.permission;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.role.RolePermission;

/**
 * {@link Permission} object factory.
 * 
 * @since 1.0.0
 */
public interface PermissionFactory extends KapuaObjectFactory {

    /**
     * Instantiate a new {@link Permission} implementing object with the provided parameters.
     * 
     * @param domain
     *            The {@link Domain} of the new {@link Permission}.
     * @param action
     *            The {@link Action} of the new {@link Permission}.
     * @param targetScopeId
     *            The target scope id of the new {@link Permission}.
     * @return A instance of the implementing class of {@link Permission}.
     * @since 1.0.0
     */
    public Permission newPermission(Domain domain, Actions action, KapuaId targetScopeId);

    // /**
    // * Instantiate a new {@link Permission} implementing object with the provided parameters.
    // *
    // * @param domain
    // * The domain of the new {@link Permission}.
    // * @param action
    // * The {@link Action} of the new {@link Permission}.
    // * @param targetScopeId
    // * The target scope id of the new {@link Permission}.
    // * @return A instance of the implementing class of {@link Permission}.
    // * @since 1.0.0
    // */
    // public Permission newPermission(String domain, Actions action, KapuaId targetScopeId);

    /**
     * Instantiate a new {@link RolePermission} implementing object with the provided parameters.
     * 
     * @param scopeId
     *            The scope id of the new {@link RolePemrission}.
     * @param permission
     *            The {@link Permission} of the {@link RolePermission}.
     * @return A instance of the implementing class of {@link RolePermission}.
     * @since 1.0.0
     */
    public RolePermission newRolePermission(KapuaId scopeId, Permission permission);

    /**
     * Parse the {@link Permission} {@link String} representation to build a new {@link Permission}.
     * 
     * @param stringPermission
     *            The {@link String} {@link Permission} representation.
     * @return A instance of the implementing class of {@link Permission}.
     * @throws KapuaException
     *             If the given string permission cannot be parsed into a {@link Permission}.
     * @since 1.0.0
     */
    public Permission parseString(String stringPermission)
            throws KapuaException;
}
