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
public class RoleXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final RoleFactory factory = locator.getFactory(RoleFactory.class);

    /**
     * Creates a new role instance
     * 
     * @return
     */
    public Role newRole() {
        return factory.newRole(null);
    }

    /**
     * Creates a new role creator instance
     * 
     * @return
     */
    public RoleCreator newRoleCreator() {
        return factory.newCreator(null);
    }

    /**
     * Creates a new role creator instance
     * 
     * @return
     */
    public RoleListResult newRoleListResult() {
        return factory.newRoleListResult();
    }
    
    public RoleQuery newQuery() {
        return factory.newQuery(null);
    }
}
