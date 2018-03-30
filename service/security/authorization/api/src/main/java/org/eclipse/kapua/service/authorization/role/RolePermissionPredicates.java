/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.role;

import org.eclipse.kapua.model.KapuaEntityPredicates;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionPredicates;

/**
 * Query predicate attribute names for {@link RolePermission} entity.
 *
 * @since 1.0.0
 */
public interface RolePermissionPredicates extends KapuaEntityPredicates {

    /**
     * Predicate for field {@link RolePermission#getRoleId()}
     *
     * @since 1.0.0
     */
    String ROLE_ID = "roleId";

    /**
     * Predicate for field {@link RolePermission#getPermission()}
     *
     * @since 1.0.0
     */
    String PERMISSION = "permission";

    /**
     * Predicate for field {@link RolePermission#getPermission()}.{@link Permission#getDomain()}
     *
     * @since 1.0.0
     */
    String PERMISSION_DOMAIN = PERMISSION + "." + PermissionPredicates.DOMAIN;

    /**
     * Predicate for field {@link RolePermission#getPermission()}.{@link Permission#getAction()}
     *
     * @since 1.0.0
     */
    String PERMISSION_ACTION = PERMISSION + "." + PermissionPredicates.ACTION;

    /**
     * Predicate for field {@link RolePermission#getPermission()}.{@link Permission#getTargetScopeId()}
     *
     * @since 1.0.0
     */
    String PERMISSION_TARGET_SCOPE_ID = PERMISSION + "." + PermissionPredicates.TARGET_SCOPE_ID;

    /**
     * Predicate for field {@link RolePermission#getPermission()}.{@link Permission#getGroupId()}
     *
     * @since 1.0.0
     */
    String PERMISSION_GROUP_ID = PERMISSION + "." + PermissionPredicates.GROUP_ID;

    /**
     * Predicate for field {@link RolePermission#getPermission()}.{@link Permission#getForwardable()}
     *
     * @since 1.0.0
     */
    String PERMISSION_FORWARDABLE = PERMISSION + "." + PermissionPredicates.FORWARDABLE;

}
