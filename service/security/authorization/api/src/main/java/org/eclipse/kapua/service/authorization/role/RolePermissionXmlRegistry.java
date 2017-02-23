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

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

@XmlRegistry
public class RolePermissionXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final RolePermissionFactory factory = locator.getFactory(RolePermissionFactory.class);

    /**
     * Creates a new {@link RolePermission} instance
     * 
     * @return
     */
    public RolePermission newRolePermission() {
        return factory.newRolePermission();
    }

    /**
     * Creates a new {@link RolePermission} instance
     * 
     * @return
     */
    public RolePermissionCreator newCreator() {
        return factory.newCreator(null);
    }

    /**
     * Creates a new {@link RolePermissionListResult} instance
     * 
     * @return
     */
    public RolePermissionListResult newRolePermissionListResult() {
        return factory.newRolePermissionListResult();
    }
    
    public RolePermissionQuery newQuery() {
        return factory.newQuery(null);
    }
}
