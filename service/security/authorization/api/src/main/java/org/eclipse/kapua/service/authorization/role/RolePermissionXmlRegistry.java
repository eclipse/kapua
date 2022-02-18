/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class RolePermissionXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final RolePermissionFactory ROLE_PERMISSION_FACTORY = LOCATOR.getFactory(RolePermissionFactory.class);

    /**
     * Creates a new {@link RolePermission} instance
     *
     * @return
     */
    public RolePermission newRolePermission() {
        return ROLE_PERMISSION_FACTORY.newEntity(null);
    }

    /**
     * Creates a new {@link RolePermission} instance
     *
     * @return
     */
    public RolePermissionCreator newCreator() {
        return ROLE_PERMISSION_FACTORY.newCreator(null);
    }

    /**
     * Creates a new {@link RolePermissionListResult} instance
     *
     * @return
     */
    public RolePermissionListResult newRolePermissionListResult() {
        return ROLE_PERMISSION_FACTORY.newListResult();
    }

    public RolePermissionQuery newQuery() {
        return ROLE_PERMISSION_FACTORY.newQuery(null);
    }
}
