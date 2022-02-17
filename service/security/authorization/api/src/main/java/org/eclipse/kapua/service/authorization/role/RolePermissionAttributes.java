/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.role;

import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionAttributes;

/**
 * Query predicate attribute names for {@link RolePermission} entity.
 *
 * @since 1.0.0
 */
public class RolePermissionAttributes extends KapuaEntityAttributes {

    /**
     * Predicate for field {@link RolePermission#getRoleId()}
     *
     * @since 1.0.0
     */
    public static final String ROLE_ID = "roleId";

    /**
     * Predicate for field {@link RolePermission#getPermission()}
     *
     * @since 1.0.0
     */
    public static final String PERMISSION = "permission";

    /**
     * Predicate for field {@link RolePermission#getPermission()}.{@link Permission#getDomain()}
     *
     * @since 1.0.0
     */
    public static final String PERMISSION_DOMAIN = PERMISSION + "." + PermissionAttributes.DOMAIN;

    /**
     * Predicate for field {@link RolePermission#getPermission()}.{@link Permission#getAction()}
     *
     * @since 1.0.0
     */
    public static final String PERMISSION_ACTION = PERMISSION + "." + PermissionAttributes.ACTION;

    /**
     * Predicate for field {@link RolePermission#getPermission()}.{@link Permission#getTargetScopeId()}
     *
     * @since 1.0.0
     */
    public static final String PERMISSION_TARGET_SCOPE_ID = PERMISSION + "." + PermissionAttributes.TARGET_SCOPE_ID;

    /**
     * Predicate for field {@link RolePermission#getPermission()}.{@link Permission#getGroupId()}
     *
     * @since 1.0.0
     */
    public static final String PERMISSION_GROUP_ID = PERMISSION + "." + PermissionAttributes.GROUP_ID;

    /**
     * Predicate for field {@link RolePermission#getPermission()}.{@link Permission#getForwardable()}
     *
     * @since 1.0.0
     */
    public static final String PERMISSION_FORWARDABLE = PERMISSION + "." + PermissionAttributes.FORWARDABLE;

}
