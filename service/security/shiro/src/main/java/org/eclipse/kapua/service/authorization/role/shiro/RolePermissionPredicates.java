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
package org.eclipse.kapua.service.authorization.role.shiro;

import org.eclipse.kapua.model.KapuaEntityPredicates;
import org.eclipse.kapua.service.authorization.role.RolePermission;

/**
 * Query predicate attribute names for {@link RolePermission} entity.
 * 
 * @since 1.0.0
 * 
 */
public interface RolePermissionPredicates extends KapuaEntityPredicates {

    /**
     * Role id
     */
    public static final String ROLE_ID = "roleId";
}
