/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

@XmlRegistry
public class RoleXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final RoleFactory factory = locator.getFactory(RoleFactory.class);

    /**
     * Creates a new {@link Role} instance.
     * 
     * @return The newly created {@link Role} instance.
     * 
     * @since 1.0.0
     */
    public Role newRole() {
        return factory.newEntity(null);
    }

    /**
     * Creates a new {@link RoleCreator} instance.
     * 
     * @return The newly created {@link RoleCreator} instance.
     * 
     * @since 1.0.0
     */
    public RoleCreator newRoleCreator() {
        return factory.newCreator(null);
    }

    /**
     * Creates a new {@link RoleListResult} instance.
     * 
     * @return The newly created {@link RoleListResult} instance.
     * 
     * @since 1.0.0
     */
    public RoleListResult newRoleListResult() {
        return factory.newListResult();
    }

    /**
     * Creates a new {@link RoleQuery} instance.
     * 
     * @return The newly created {@link RoleQuery} instance.
     * 
     * @since 1.0.0
     */
    public RoleQuery newQuery() {
        return factory.newQuery(null);
    }
}
